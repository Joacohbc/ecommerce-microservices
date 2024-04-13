package com.microecommerce.productsservice.services;

import com.microecommerce.productsservice.exceptions.EntityNotFoundException;
import com.microecommerce.productsservice.exceptions.RelatedEntityNotFoundException;
import com.microecommerce.productsservice.models.Detail;
import com.microecommerce.productsservice.models.IGetId;
import com.microecommerce.productsservice.services.interfaces.IDetailService;
import com.microecommerce.productsservice.repositories.DetailRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailService implements IDetailService {

    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    @Override
    public List<Detail> getAll() {
        return detailRepository.findAll();
    }

    @Override
    public Detail getById(Long id) throws EntityNotFoundException {
        return detailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Detail not found"));
    }

    @Override
    public List<Detail> getByIds(List<Long> ids) {
        return detailRepository.findAllById(ids);
    }

    @Override
    public Detail create(@Valid Detail entity) throws RelatedEntityNotFoundException {
        return createBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Detail> createBatch(@Valid List<Detail> entities) throws RelatedEntityNotFoundException {
        if(entities.isEmpty()) return Collections.emptyList();
        entities.forEach(detail -> detail.setId(null));
        validateDuplicatedNames(entities);

        return detailRepository.saveAll(entities);
    }

    @Override
    public Detail update(@Valid Detail entity) throws RelatedEntityNotFoundException {
        return updateBatch(Collections.singletonList(entity)).get(0);
    }

    @Override
    public List<Detail> updateBatch(@Valid List<Detail> entities) throws RelatedEntityNotFoundException {
        if(entities.isEmpty()) return Collections.emptyList();
        if(!IGetId.allHaveId(entities)) throw new RelatedEntityNotFoundException("All details must have an ID to be updated");
        validateDuplicatedNames(entities);
        return detailRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long id) {
        detailRepository.deleteById(id);
    }

    private void validateDuplicatedNames(List<Detail> details) throws RelatedEntityNotFoundException {
        var names = details.stream().map(Detail::getName).collect(Collectors.toList());
        var repeatedName = detailRepository.existsByNameIn(names);
        if(repeatedName) throw new RelatedEntityNotFoundException("Some details have repeated name at database");

        // Check if there are repeated Name in the request (only if there are more than one product)
        if(!details.isEmpty() && (details.size() != new HashSet<>(names).size()))
            // TODO: Change the exception type to new Exception type
            throw new RelatedEntityNotFoundException("Some details have repeated name in the request");
    }
}
