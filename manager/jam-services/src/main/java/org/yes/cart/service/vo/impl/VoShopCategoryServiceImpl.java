package org.yes.cart.service.vo.impl;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import org.yes.cart.domain.dto.CategoryDTO;
import org.yes.cart.domain.dto.ShopDTO;
import org.yes.cart.domain.vo.VoCategory;
import org.yes.cart.service.dto.DtoCategoryService;
import org.yes.cart.service.dto.DtoShopService;
import org.yes.cart.service.federation.FederationFacade;
import org.yes.cart.service.vo.VoShopCategoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor_Azarny on 5/3/2016.
 */
public class VoShopCategoryServiceImpl implements VoShopCategoryService {

    private final DtoCategoryService dtoCategoryService;
    private final Assembler simpleVoCategoryAssembler;
    private final FederationFacade federationFacade;
    private final DtoShopService dtoShopService;

    /**
     * Construct category shop service.
     * @param dtoCategoryService dto service to use
     * @param federationFacade federation service to use.
     */
    public VoShopCategoryServiceImpl(final DtoCategoryService dtoCategoryService,
                                     final DtoShopService dtoShopService,
                                     final FederationFacade federationFacade) {
        this.dtoCategoryService = dtoCategoryService;
        this.federationFacade = federationFacade;
        this.dtoShopService = dtoShopService;
        this.simpleVoCategoryAssembler = DTOAssembler.newAssembler(VoCategory.class, CategoryDTO.class);
    }



    /** {@inheritDoc} */
    @Override
    public List<VoCategory> getAllByShopId(long shopId) throws Exception {
        final ShopDTO shopDTO = dtoShopService.getById(shopId);
        if (federationFacade.isShopAccessibleByCurrentManager(shopDTO.getCode())) {
            final List<CategoryDTO> categoryDTOs = dtoCategoryService.getAllByShopId(shopId);
            final List<VoCategory> voCategories = new ArrayList<>(categoryDTOs.size());
            simpleVoCategoryAssembler.assembleDtos(voCategories, categoryDTOs, null ,null);
            return voCategories;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public List<VoCategory> update(long shopId, List<VoCategory> voCategories) throws Exception {
        final ShopDTO shopDTO = dtoShopService.getById(shopId);
        if (federationFacade.isShopAccessibleByCurrentManager(shopDTO.getCode())) {
            List<VoCategory> oldAssigned = getAllByShopId(shopId);
            for (VoCategory cat : oldAssigned) {
                dtoCategoryService.unassignFromShop(cat.getCategoryId(), shopId);
            }
            for (VoCategory cat : voCategories) {
                dtoCategoryService.assignToShop(cat.getCategoryId(), shopId);
            }
            return getAllByShopId(shopId);
        }
        return null;
    }

}
