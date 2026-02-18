/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda_SamuelMena.controller;

import Tienda_SamuelMena.service.ProductoService;
import Tienda_SamuelMena.service.CategoriaService;
import Tienda_SamuelMena.domain.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/producto") // en minúscula
public class ProductoController {

    @Autowired
    private ProductoService productoService; // en minúscula

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var productos = productoService.getProductos(true);
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productos.size());

        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        model.addAttribute("producto", new Producto()); // 👈 ESTA ES LA LÍNEA QUE FALTA

        return "producto/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Producto producto,
            @RequestParam MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {
        productoService.save(producto, imagenFile);
        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            productoService.delete(idProducto);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "producto.error01"; // en minúscula
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "producto.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "producto.error03";
        }
        redirectAttributes.addFlashAttribute(titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/producto/listado";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable("idProducto") Integer idProducto,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Producto> productoOpt = productoService.getProducto(idProducto);
        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("producto.error01", null, Locale.getDefault()));
            return "redirect:/producto/listado";
        }
        model.addAttribute("producto", productoOpt.get());
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "producto/modifica"; // sin barra inicial y en minúscula
    }
}
