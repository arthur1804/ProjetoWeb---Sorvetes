package com.ProjetoWeb.Sorveteria.service;

import com.ProjetoWeb.Sorveteria.model.Sorvete;
import com.ProjetoWeb.Sorveteria.repository.SorveteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SorveteService {

    @Autowired
    private SorveteRepository sorveteRepository;

    public List<Sorvete> listarAtivos() {
        return sorveteRepository.findByIsDeletedIsNull();
    }

    public Optional<Sorvete> findById(Long id) {
        return sorveteRepository.findById(id);
    }

    public void create(Sorvete sorvete) {
        sorveteRepository.save(sorvete);
    }

    public void update(Sorvete sorvete) {
        sorveteRepository.save(sorvete);
    }

    public void delete(Long id) {
        Optional<Sorvete> sorveteOpt = findById(id);
        if (sorveteOpt.isPresent()) {
            Sorvete sorvete = sorveteOpt.get();
            sorvete.setIsDeleted(LocalDateTime.now());
            update(sorvete);
        }
    }
}
