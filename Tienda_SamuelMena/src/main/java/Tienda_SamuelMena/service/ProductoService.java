/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda_SamuelMena.service;

import Tienda_SamuelMena.domain.Producto;
import Tienda_SamuelMena.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.dao.DataIntegrityViolationException;




@Service
public class ProductoService {

    // Permite crear una única instancia de ProductoRepository, y la crea automáticamente
    @Autowired
    private ProductoRepository ProductoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) { // Sólo activos...
            return ProductoRepository.findByActivoTrue();
        }
        return ProductoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return ProductoRepository.findById(idProducto);
    }

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Transactional
    public void save(Producto Producto, MultipartFile imagenFile) {
        Producto = ProductoRepository.save(Producto);
        if (!imagenFile.isEmpty()) { // Si no está vacío... pasaron una imagen...
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "Producto",
                        Producto.getIdProducto());
                Producto.setRutaImagen(rutaImagen);
                ProductoRepository.save(Producto);
            } catch (IOException e) {
            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!ProductoRepository.existsById(idProducto)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La categoría con ID " + idProducto + " no existe.");
        }
        try {
            ProductoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la categoría. Tiene datos asociados.", e);
        }
    }

}
