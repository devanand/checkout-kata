package com.haiilo.checkout.application;



public class CheckoutServiceTest {
//    private final Catalog catalog = new InMemoryCatalog();
//    private final OfferLoader offerLoader =
//
//    private final OfferCatalog offerCatalog = new InMemoryOfferCatalog(objectMapper);
//    private final PricingService pricingService = new PricingService(offerCatalog);
//    private final CheckoutService checkoutService = new CheckoutService(catalog, pricingService);
//
//    @Test
//    void returnsZeroForEmptyCart() {
//        Cart cart = new Cart();
//        Money total = checkoutService.checkout(cart);
//        assertEquals(Money.zero(), total);
//    }
//
//    @Test
//    void calculatesTotalForSingleItem() {
//        Cart cart = new Cart();
//        cart.add(ProductId.of("APPLE"), 1);
//        Money total = checkoutService.checkout(cart);
//        assertEquals(Money.eur("0.30"), total);
//    }
//
//    @Test
//    void calculatesTotalForMultipleQuantitiesOfSameItem() {
//        Cart cart = new Cart();
//        cart.add(ProductId.of("APPLE"), 3);
//        Money total = checkoutService.checkout(cart);
//        assertEquals(Money.eur("0.90"), total);
//    }
//
//    @Test
//    void calculatesTotalForMixedItems() {
//        Cart cart = new Cart();
//        cart.add(ProductId.of("APPLE"), 2);
//        cart.add(ProductId.of("BANANA"), 1);
//        Money total = checkoutService.checkout(cart);
//        assertEquals(Money.eur("0.80"), total);
//    }
//
//    @Test
//    void rejectsUnknownProduct() {
//        Cart cart = new Cart();
//        cart.add(ProductId.of("MANGO"), 1);
//        assertThrows(UnknownProductException.class, () -> checkoutService.checkout(cart));
//    }
//
//    @Test
//    void rejectsNullCart() {
//
//        assertThrows(NullPointerException.class, () -> checkoutService.checkout(null));
//    }
}
