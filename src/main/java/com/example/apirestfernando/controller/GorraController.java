package com.example.apirestfernando.controller;
import com.example.apirestfernando.model.Gorra;
import com.example.apirestfernando.model.Marca;
import com.example.apirestfernando.model.MensajeRespuesta;
import com.example.apirestfernando.repository.MarcaRepository;
import com.example.apirestfernando.service.GorraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gorras")
public class GorraController {

    @Autowired
    private GorraService gorraService;

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public List<Gorra> getTodasLasGorras() {
        return gorraService.getTodasLasGorras();
    }

    @GetMapping("/{id}")
    public Gorra getGorraById(@PathVariable int id) {
        return gorraService.getGorraPorId(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Gorra guardarGorra(@RequestParam("precio") Double precio,
                            @RequestParam("descripcion") String descripcion,
                            @RequestParam("marca_id") Integer marcaId,
                            @RequestParam("nombre_imagen") String nombreImagen,
                            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        Gorra gorra = new Gorra();
        gorra.setPrecio(precio.floatValue());
        gorra.setDescripcion(descripcion);
        gorra.setNombreImagen(nombreImagen);
    
        Marca marca = marcaRepository.findById(marcaId).orElse(null);
        gorra.setMarca(marca);
    
        if (imagen != null) {
            Path path = Paths.get("src/main/resources/static/img/" + nombreImagen);
            Files.write(path, imagen.getBytes());
            gorra.setNombreImagen(nombreImagen);
            gorra.setImagen(imagen.getBytes());
        }
    
        return gorraService.guardarGorra(gorra);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Gorra modificarGorra(@PathVariable int id,
                            @RequestParam(value = "precio", required = false) Double precio,
                            @RequestParam(value = "descripcion", required = false) String descripcion,
                            @RequestParam(value = "marca_id", required = false) Integer marcaId,
                            @RequestParam(value = "nombre_imagen", required = false) String nombreImagen,
                            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        Gorra gorraExistente = gorraService.getGorraPorId(id);
        if (gorraExistente != null) {
            if (precio != null) {
                gorraExistente.setPrecio(precio.floatValue());
            }
            if (descripcion != null) {
                gorraExistente.setDescripcion(descripcion);
            }
            if (marcaId != null) {
                Marca marca = marcaRepository.findById(marcaId).orElse(null);
                gorraExistente.setMarca(marca);
            }
            if (imagen != null) {
                String imagenNombre = (nombreImagen != null) ? nombreImagen : gorraExistente.getNombreImagen();
                Path path = Paths.get("src/main/resources/static/img/" + imagenNombre);
                Files.write(path, imagen.getBytes());
                gorraExistente.setNombreImagen(imagenNombre);
                gorraExistente.setImagen(imagen.getBytes());
            }

            return gorraService.guardarGorra(gorraExistente);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeRespuesta> borrarGorra(@PathVariable int id) {
        boolean gorraBorrada = gorraService.borrarGorra(id);
        if (gorraBorrada) {
            return new ResponseEntity<>(new MensajeRespuesta("Gorra borrada correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MensajeRespuesta("Gorra no encontrada"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/precio")
    public List<Gorra> getGorrasPrecio() {
        return gorraService.getGorrasPrecio("precio");
    }

    @GetMapping("/marcas")
    public List<Gorra> getGorrasOrdenadas(@RequestParam(required = false, defaultValue = "marca_id") String orderBy) {
        return gorraService.getGorrasOrdenadas(orderBy);
    }

    @GetMapping("/html")
    public ModelAndView getGorrasHtml() {
        ModelAndView modelAndView = new ModelAndView("gorras");
        modelAndView.addObject("gorras", gorraService.getTodasLasGorras());
        return modelAndView;
    }
}