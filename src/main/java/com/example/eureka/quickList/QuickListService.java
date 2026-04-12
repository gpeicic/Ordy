package com.example.eureka.quickList;

import com.example.eureka.quickList.dto.QuickListItemDetail;

import java.util.List;

public interface QuickListService {
    QuickList create(QuickList quickList, List<QuickListItem> items);
    List<QuickList> getByCompanyId(Long companyId);
    QuickList getById(Long id);
    List<QuickListItemDetail> getItems(Long quickListId);
    void delete(Long id);
    QuickList update(Long id, String name);
}
