package com.example.eureka.orders.query;

import com.example.eureka.catalogue.CatalogueItem;
import com.example.eureka.catalogue.CatalogueItemMapper;
import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.invoice.InvoiceItemMapper;
import com.example.eureka.orders.Order;
import com.example.eureka.orders.OrderItem;
import com.example.eureka.orders.OrderItemMapper;
import com.example.eureka.orders.OrderMapper;
import com.example.eureka.orders.dto.OrderItemDetail;
import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CatalogueItemMapper catalogueItemMapper;
    private final InvoiceItemMapper invoiceItemMapper;

    public OrderQueryServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                                 CatalogueItemMapper catalogueItemMapper, InvoiceItemMapper invoiceItemMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.catalogueItemMapper = catalogueItemMapper;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByCompany(Long companyId) {
        return orderMapper.findByCompanyId(companyId);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }

    @Override
    public OrderWithSupplierNameDTO findLatestByCompanyId(Long companyId) {
        return orderMapper.findLatestByCompanyId(companyId);
    }

    @Override
    public List<OrderSummary> getOrderSummariesByCompany(Long companyId) {
        List<OrderSummary> summaries = orderMapper.findSummariesByCompanyId(companyId);
        summaries.forEach(summary -> summary.setItems(buildItemDetails(summary.getId())));
        return summaries;
    }

    @Override
    public List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId, Long companyId, String name) {
        List<CatalogueItem> catalogueItems = catalogueItemMapper.findBySupplierId(supplierId);

        if(!catalogueItems.isEmpty()){
            return catalogueItemMapper.fuzzySearchByName(supplierId, name);
        }
        return invoiceItemMapper.findDistinctItemsBySupplierAndCompany(supplierId,companyId,name);
    }

    private List<OrderItemDetail> buildItemDetails(Long orderId) {
        return orderItemMapper.findByOrderId(orderId).stream()
                .map(this::toOrderItemDetail)
                .collect(Collectors.toList());
    }

    private OrderItemDetail toOrderItemDetail(OrderItem item) {
        String name = orderItemMapper.findProductNameByCatalogueItemId(item.getCatalogueItemId());
        OrderItemDetail detail = new OrderItemDetail();
        detail.setId(item.getId());
        detail.setName(name != null ? name : "Nepoznat artikl");
        detail.setQuantity(item.getQuantity());
        return detail;
    }

}