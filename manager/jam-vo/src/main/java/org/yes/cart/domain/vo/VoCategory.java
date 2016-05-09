/*
 * Copyright 2009 - 2016 Denys Pavlov, Igor Azarnyi
 *
 *    Licensed under the Apache License, Version 2.0 (the 'License');
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an 'AS IS' BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.yes.cart.domain.vo;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.yes.cart.domain.dto.AttrValueCategoryDTO;

/**
 * Created by Igor_Azarny on 4/13/2016.
 */
@Dto
public class VoCategory {

    private static final long serialVersionUID = 20160413L;

    @DtoField(value = "categoryId", readOnly = true)
    private long categoryId;

    @DtoField(value = "parentId")
    private long parentId;

    @DtoField(value = "rank")
    private int rank;

    @DtoField(value = "productTypeId", readOnly = true)
    private Long productTypeId;

    @DtoField(value = "productTypeName", readOnly = true)
    private String productTypeName;

    @DtoField(value = "name")
    private String name;

    @DtoField(value = "guid")
    private String guid;

    @DtoField(value = "displayNames")
    private Map<String, String> displayNames;

    @DtoField(value = "description")
    private String description;

    @DtoField(value = "uitemplate")
    private String uitemplate;

    @DtoField(value = "availablefrom")
    private Date availablefrom;

    @DtoField(value = "availableto")
    private Date availableto;

    @DtoField(value = "uri")
    private String uri;

    @DtoField(value = "title")
    private String title;

    @DtoField(value = "metakeywords")
    private String metakeywords;

    @DtoField(value = "metadescription")
    private String metadescription;

    @DtoField(value = "displayTitles")
    private Map<String, String> displayTitles;

    @DtoField(value = "displayMetakeywords")
    private Map<String, String> displayMetakeywords;

    @DtoField(value = "displayMetadescriptions")
    private Map<String, String> displayMetadescriptions;

    @DtoField(value = "navigationByAttributes")
    private Boolean navigationByAttributes;

    @DtoField(value = "navigationByBrand")
    private Boolean navigationByBrand;

    @DtoField(value = "navigationByPrice")
    private Boolean navigationByPrice;

    @DtoField(value = "navigationByPriceTiers")
    private String navigationByPriceTiers;

    @DtoField(value = "children")
    private List<VoCategory> children;

    //TODO VO @DtoField(value = "attributes",readOnly = true )
    //private Set<AttrValueCategoryDTO> attributes;

    public List<VoCategory> getChildren() {
        return children;
    }

    public void setChildren(List<VoCategory> children) {
        this.children = children;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Map<String, String> getDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(Map<String, String> displayNames) {
        this.displayNames = displayNames;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUitemplate() {
        return uitemplate;
    }

    public void setUitemplate(String uitemplate) {
        this.uitemplate = uitemplate;
    }

    public Date getAvailablefrom() {
        return availablefrom;
    }

    public void setAvailablefrom(Date availablefrom) {
        this.availablefrom = availablefrom;
    }

    public Date getAvailableto() {
        return availableto;
    }

    public void setAvailableto(Date availableto) {
        this.availableto = availableto;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetakeywords() {
        return metakeywords;
    }

    public void setMetakeywords(String metakeywords) {
        this.metakeywords = metakeywords;
    }

    public String getMetadescription() {
        return metadescription;
    }

    public void setMetadescription(String metadescription) {
        this.metadescription = metadescription;
    }

    public Map<String, String> getDisplayTitles() {
        return displayTitles;
    }

    public void setDisplayTitles(Map<String, String> displayTitles) {
        this.displayTitles = displayTitles;
    }

    public Map<String, String> getDisplayMetakeywords() {
        return displayMetakeywords;
    }

    public void setDisplayMetakeywords(Map<String, String> displayMetakeywords) {
        this.displayMetakeywords = displayMetakeywords;
    }

    public Map<String, String> getDisplayMetadescriptions() {
        return displayMetadescriptions;
    }

    public void setDisplayMetadescriptions(Map<String, String> displayMetadescriptions) {
        this.displayMetadescriptions = displayMetadescriptions;
    }

    public Boolean getNavigationByAttributes() {
        return navigationByAttributes;
    }

    public void setNavigationByAttributes(Boolean navigationByAttributes) {
        this.navigationByAttributes = navigationByAttributes;
    }

    public Boolean getNavigationByBrand() {
        return navigationByBrand;
    }

    public void setNavigationByBrand(Boolean navigationByBrand) {
        this.navigationByBrand = navigationByBrand;
    }

    public Boolean getNavigationByPrice() {
        return navigationByPrice;
    }

    public void setNavigationByPrice(Boolean navigationByPrice) {
        this.navigationByPrice = navigationByPrice;
    }

    public String getNavigationByPriceTiers() {
        return navigationByPriceTiers;
    }

    public void setNavigationByPriceTiers(String navigationByPriceTiers) {
        this.navigationByPriceTiers = navigationByPriceTiers;
    }
}
