package org.yes.cart.service.vo.impl;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.yes.cart.domain.dto.ProdTypeAttributeViewGroupDTO;
import org.yes.cart.domain.dto.ProductTypeAttrDTO;
import org.yes.cart.domain.dto.ProductTypeDTO;
import org.yes.cart.domain.misc.MutablePair;
import org.yes.cart.domain.vo.VoProductType;
import org.yes.cart.domain.vo.VoProductTypeAttr;
import org.yes.cart.domain.vo.VoProductTypeInfo;
import org.yes.cart.domain.vo.VoProductTypeViewGroup;
import org.yes.cart.service.dto.DtoAttributeService;
import org.yes.cart.service.dto.DtoProdTypeAttributeViewGroupService;
import org.yes.cart.service.dto.DtoProductTypeAttrService;
import org.yes.cart.service.dto.DtoProductTypeService;
import org.yes.cart.service.federation.FederationFacade;
import org.yes.cart.service.vo.VoAssemblySupport;
import org.yes.cart.service.vo.VoProductTypeService;

import java.util.*;

/**
 * User: denispavlov
 * Date: 22/08/2016
 * Time: 12:48
 */
public class VoProductTypeServiceImpl implements VoProductTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(VoProductTypeServiceImpl.class);

    private final DtoProductTypeService dtoProductTypeService;
    private final DtoProdTypeAttributeViewGroupService dtoProdTypeAttributeViewGroupService;
    private final DtoProductTypeAttrService dtoProductTypeAttrService;
    private final DtoAttributeService dtoAttributeService;

    private final FederationFacade federationFacade;
    private final VoAssemblySupport voAssemblySupport;

    public VoProductTypeServiceImpl(final DtoProductTypeService dtoProductTypeService,
                                    final DtoProdTypeAttributeViewGroupService dtoProdTypeAttributeViewGroupService,
                                    final DtoProductTypeAttrService dtoProductTypeAttrService,
                                    final DtoAttributeService dtoAttributeService,
                                    final FederationFacade federationFacade,
                                    final VoAssemblySupport voAssemblySupport) {
        this.dtoProductTypeService = dtoProductTypeService;
        this.dtoProdTypeAttributeViewGroupService = dtoProdTypeAttributeViewGroupService;
        this.dtoProductTypeAttrService = dtoProductTypeAttrService;
        this.dtoAttributeService = dtoAttributeService;
        this.federationFacade = federationFacade;
        this.voAssemblySupport = voAssemblySupport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VoProductTypeInfo> getFilteredTypes(final String filter, final int max) throws Exception {

        final List<VoProductTypeInfo> results = new ArrayList<>();

        final List<ProductTypeDTO> batch = dtoProductTypeService.findBy(filter, 0, max);
        if (!batch.isEmpty()) {
            results.addAll(voAssemblySupport.assembleVos(VoProductTypeInfo.class, ProductTypeDTO.class, batch));
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VoProductType getTypeById(final long id) throws Exception {
        final ProductTypeDTO typeDTO = dtoProductTypeService.getById(id);
        if (typeDTO != null /* && federationFacade.isCurrentUserSystemAdmin() */) {
            final VoProductType type = voAssemblySupport.assembleVo(VoProductType.class, ProductTypeDTO.class, new VoProductType(), typeDTO);
            final List<ProdTypeAttributeViewGroupDTO> groups = dtoProdTypeAttributeViewGroupService.getByProductTypeId(id);
            final List<VoProductTypeViewGroup> voGroups = voAssemblySupport.assembleVos(VoProductTypeViewGroup.class, ProdTypeAttributeViewGroupDTO.class, groups);
            type.setViewGroups(voGroups);
            return type;
        } else {
            throw new AccessDeniedException("Access is denied");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VoProductType updateType(final VoProductType vo) throws Exception {
        final ProductTypeDTO typeDTO = dtoProductTypeService.getById(vo.getProducttypeId());
        if (typeDTO != null && federationFacade.isCurrentUserSystemAdmin()) {
            dtoProductTypeService.update(
                    voAssemblySupport.assembleDto(ProductTypeDTO.class, VoProductType.class, typeDTO, vo)
            );

            final List<ProdTypeAttributeViewGroupDTO> groups = dtoProdTypeAttributeViewGroupService.getByProductTypeId(vo.getProducttypeId());
            final Map<Long, ProdTypeAttributeViewGroupDTO> existing = new HashMap<>();
            for (final ProdTypeAttributeViewGroupDTO group : groups) {
                existing.put(group.getProdTypeAttributeViewGroupId(), group);
            }

            if (vo.getViewGroups() != null) {
                for (final VoProductTypeViewGroup voGroup : vo.getViewGroups()) {
                    final ProdTypeAttributeViewGroupDTO dtoToUpdate = existing.get(voGroup.getProdTypeAttributeViewGroupId());
                    voGroup.setProducttypeId(vo.getProducttypeId()); // ensure we do not change the product type
                    if (dtoToUpdate != null) {
                        // update mode
                        existing.remove(dtoToUpdate.getProdTypeAttributeViewGroupId());

                        dtoProdTypeAttributeViewGroupService.update(
                            voAssemblySupport.assembleDto(ProdTypeAttributeViewGroupDTO.class, VoProductTypeViewGroup.class, dtoToUpdate, voGroup)
                        );

                    } else {
                        // insert mode

                        final ProdTypeAttributeViewGroupDTO newGroup = dtoProdTypeAttributeViewGroupService.getNew();

                        dtoProdTypeAttributeViewGroupService.create(
                                voAssemblySupport.assembleDto(ProdTypeAttributeViewGroupDTO.class, VoProductTypeViewGroup.class, newGroup, voGroup)
                        );
                    }
                }
            }

            for (final ProdTypeAttributeViewGroupDTO remove : existing.values()) {
                dtoProdTypeAttributeViewGroupService.remove(remove.getProdTypeAttributeViewGroupId());
            }

        } else {
            throw new AccessDeniedException("Access is denied");
        }
        return getTypeById(vo.getProducttypeId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VoProductType createType(final VoProductTypeInfo vo) throws Exception {
        if (federationFacade.isCurrentUserSystemAdmin()) {
            ProductTypeDTO typeDTO = dtoProductTypeService.getNew();
            typeDTO = dtoProductTypeService.create(
                    voAssemblySupport.assembleDto(ProductTypeDTO.class, VoProductTypeInfo.class, typeDTO, vo)
            );
            return getTypeById(typeDTO.getProducttypeId());
        } else {
            throw new AccessDeniedException("Access is denied");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeType(final long id) throws Exception {
        if (federationFacade.isCurrentUserSystemAdmin()) {
            final List<ProdTypeAttributeViewGroupDTO> groups = dtoProdTypeAttributeViewGroupService.getByProductTypeId(id);
            if (CollectionUtils.isNotEmpty(groups)) {
                for (final ProdTypeAttributeViewGroupDTO group : groups) {
                    dtoProdTypeAttributeViewGroupService.remove(group.getProdTypeAttributeViewGroupId());
                }
            }
            dtoProductTypeService.remove(id);
        } else {
            throw new AccessDeniedException("Access is denied");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<VoProductTypeAttr> getTypeAttributes(final long typeId) throws Exception {

        final List<ProductTypeAttrDTO> attributes = dtoProductTypeAttrService.getByProductTypeId(typeId);

        return voAssemblySupport.assembleVos(VoProductTypeAttr.class, ProductTypeAttrDTO.class, attributes);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<VoProductTypeAttr> updateTypeAttributes(final List<MutablePair<VoProductTypeAttr, Boolean>> vo) throws Exception {

        long typeId = 0L;
        if (federationFacade.isCurrentUserSystemAdmin()) {

            final VoAssemblySupport.VoAssembler<VoProductTypeAttr, ProductTypeAttrDTO> asm =
                    voAssemblySupport.with(VoProductTypeAttr.class, ProductTypeAttrDTO.class);


            Map<Long, ProductTypeAttrDTO> existing = Collections.emptyMap();
            for (final MutablePair<VoProductTypeAttr, Boolean> item : vo) {
                if (typeId == 0L) {
                    typeId = item.getFirst().getProducttypeId();
                    existing = mapAvById((List) dtoProductTypeAttrService.getByProductTypeId(typeId));
                } else if (typeId != item.getFirst().getProducttypeId()) {
                    throw new AccessDeniedException("Access is denied");
                }

                if (Boolean.valueOf(item.getSecond())) {
                    if (item.getFirst().getProductTypeAttrId() > 0L) {
                        // delete mode
                        dtoProductTypeAttrService.remove(item.getFirst().getProductTypeAttrId());
                    }
                } else if (item.getFirst().getProductTypeAttrId() > 0L) {
                    // update mode
                    final ProductTypeAttrDTO dto = existing.get(item.getFirst().getProductTypeAttrId());
                    if (dto != null) {
                        asm.assembleDto(dto, item.getFirst());
                        dtoProductTypeAttrService.update(dto);
                    } else {
                        LOG.warn("Update skipped for inexistent ID {}", item.getFirst().getProductTypeAttrId());
                    }
                } else {
                    // insert mode
                    final ProductTypeAttrDTO dto = dtoProductTypeAttrService.getNew();
                    dto.setProducttypeId(typeId);
                    dto.setAttributeDTO(dtoAttributeService.getById(item.getFirst().getAttribute().getAttributeId()));
                    asm.assembleDto(dto, item.getFirst());
                    this.dtoProductTypeAttrService.create(dto);
                }

            }

        } else {
            throw new AccessDeniedException("Access is denied");
        }

        return getTypeAttributes(typeId);
    }

    private Map<Long, ProductTypeAttrDTO> mapAvById(final List<ProductTypeAttrDTO> entityAttributes) {
        Map<Long, ProductTypeAttrDTO> map = new HashMap<>();
        for (final ProductTypeAttrDTO dto : entityAttributes) {
            map.put(dto.getProductTypeAttrId(), dto);
        }
        return map;
    }
}
