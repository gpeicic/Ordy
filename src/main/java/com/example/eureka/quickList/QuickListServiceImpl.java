package com.example.eureka.quickList;

import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.quickList.dto.QuickListItemDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuickListServiceImpl implements QuickListService {

    private static final Logger log = LoggerFactory.getLogger(QuickListServiceImpl.class);

    private final QuickListMapper quickListMapper;
    private final QuickListItemMapper quickListItemMapper;

    public QuickListServiceImpl(QuickListMapper quickListMapper, QuickListItemMapper quickListItemMapper) {
        this.quickListMapper = quickListMapper;
        this.quickListItemMapper = quickListItemMapper;
    }

    @Override
    public QuickList create(QuickList quickList, List<QuickListItem> items) {
        if (quickList.getName() == null || quickList.getName().isBlank()) {
            throw new ValidationException("Naziv liste je obavezan");
        }
        if (items == null || items.isEmpty()) {
            throw new ValidationException("Lista mora imati najmanje jedan artikl");
        }
        quickListMapper.insert(quickList);
        items.forEach(item -> {
            item.setQuickListId(quickList.getId());
            quickListItemMapper.insert(item);
        });
        log.info("Brza lista kreirana — id: {}, name: {}, companyId: {}", quickList.getId(), quickList.getName(), quickList.getCompanyId());
        return quickList;
    }

    @Override
    public List<QuickList> getByCompanyId(Long companyId) {
        if (companyId == null) {
            throw new ValidationException("companyId je obavezan");
        }
        return quickListMapper.findByCompanyId(companyId);
    }

    @Override
    public QuickList getById(Long id) {
        QuickList quickList = quickListMapper.findById(id);
        if (quickList == null) {
            throw new ResourceNotFoundException("Brza lista nije pronađena: " + id);
        }
        return quickList;
    }

    @Override
    public List<QuickListItemDetail> getItems(Long quickListId) {
        return quickListItemMapper.findByQuickListId(quickListId);
    }

    @Override
    public void delete(Long id) {
        QuickList existing = quickListMapper.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Brza lista nije pronađena: " + id);
        }
        quickListMapper.delete(id);
        log.info("Brza lista obrisana — id: {}", id);
    }

    @Override
    public QuickList update(Long id, String name) {
        QuickList existing = quickListMapper.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Brza lista nije pronađena: " + id);
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Naziv liste je obavezan");
        }
        existing.setName(name);
        quickListMapper.update(existing);
        log.info("Brza lista ažurirana — id: {}, novo ime: {}", id, name);
        return existing;
    }
}
