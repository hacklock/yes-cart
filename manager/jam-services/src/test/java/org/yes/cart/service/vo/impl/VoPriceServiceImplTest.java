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
package org.yes.cart.service.vo.impl;

import org.junit.Before;
import org.junit.Test;
import org.yes.cart.BaseCoreDBTestCase;
import org.yes.cart.domain.vo.VoPriceList;
import org.yes.cart.service.vo.VoPriceService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 25/09/2019
 * Time: 11:05
 */
public class VoPriceServiceImplTest extends BaseCoreDBTestCase {

    private VoPriceService voPriceService;

    @Before
    public void setUp() {
        voPriceService = (VoPriceService) ctx().getBean("voPriceService");
        super.setUp();
    }

    @Test
    public void testGetPrices() throws Exception {

        final List<VoPriceList> pl = voPriceService.getFilteredPrices(10L, "EUR", "XXXX", 10);
        assertNotNull(pl);
        assertTrue(pl.isEmpty());

        final List<VoPriceList> pl2 = voPriceService.getFilteredPrices(10L, "EUR", "CC_TEST1", 10);
        assertNotNull(pl2);
        assertFalse(pl2.isEmpty());
        assertEquals("CC_TEST1", pl2.get(0).getSkuCode());

    }

    @Test
    public void testPricesCRUD() throws Exception {

        final VoPriceList priceList = new VoPriceList();
        priceList.setCurrency("EUR");
        priceList.setSkuCode("TESTCRUD");
        priceList.setShopCode("SHOIP1");
        priceList.setQuantity(BigDecimal.ONE);
        priceList.setRegularPrice(new BigDecimal("9.99"));

        final VoPriceList created = voPriceService.createPrice(priceList);
        assertTrue(created.getSkuPriceId() > 0L);

        VoPriceList afterCreated = voPriceService.getPriceById(created.getSkuPriceId());
        assertNotNull(afterCreated);
        assertEquals("TESTCRUD", afterCreated.getSkuCode());
        assertTrue(new BigDecimal("9.99").compareTo(afterCreated.getRegularPrice()) == 0);

        afterCreated.setRegularPrice(new BigDecimal("19.99"));

        final VoPriceList updated = voPriceService.updatePrice(afterCreated);
        assertTrue(new BigDecimal("19.99").compareTo(updated.getRegularPrice()) == 0);

        assertFalse(voPriceService.getFilteredPrices(10L, "EUR","!TESTCRUD", 10).isEmpty());

        voPriceService.removePrice(updated.getSkuPriceId());

        assertTrue(voPriceService.getFilteredPrices(10L, "EUR","!TESTCRUD", 10).isEmpty());

    }
}