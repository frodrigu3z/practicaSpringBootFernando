package com.example.apirestfernando.service;
import com.example.apirestfernando.model.Gorra;
import com.example.apirestfernando.model.Marca;
import com.example.apirestfernando.repository.GorraRepository;
import com.example.apirestfernando.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;

@Service
public class GorraService {

    @Autowired
    private GorraRepository gorraRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Gorra> getTodasLasGorras() {
        return gorraRepository.findAll();
    }

    public Gorra getGorraPorId(int id) {
        return gorraRepository.findById(id).orElse(null);
    }

    public Gorra guardarGorra(Gorra gorra) {
        Marca marca = marcaRepository.findById(gorra.getMarca().getId()).orElse(null);
        gorra.setMarca(marca);
        return gorraRepository.save(gorra);
    }

    public boolean borrarGorra(int id) {
        if (gorraRepository.existsById(id)) {
            gorraRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Gorra> getGorrasPrecio(String campo) {
        return gorraRepository.findAll(Sort.by(campo));
    }

    public List<Gorra> getGorrasOrdenadas(String orderBy) {
        return gorraRepository.findAll(Sort.by(orderBy));
    }
}