package com.tienda.controller;

import com.tienda.domain.Producto;
import com.tienda.service.ProductoService;
import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;
import com.tienda.service.impl.FirebaseStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequestMapping("/producto")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
  @Autowired
private CategoriaService categoriaService;
@GetMapping("/listado")
public String inicio(Model model) {
    var productos = productoService.getProductos(false);
    var categorias = categoriaService.getCategorias(false);         
    model.addAttribute("categorias", categorias);
    model.addAttribute("productos", productos);
    model.addAttribute("totalProductos", productos.size());
    model.addAttribute("producto", new Producto()); // <--- necesario para th:object
    return "/producto/listado";
}

    
    @GetMapping("/nuevo")
    public String productoNuevo(Producto producto) {
        return "/producto/modifica";
    }

    @Autowired
    private FirebaseStorageServiceImpl firebaseStorageService;
    
    @PostMapping("/guardar")
    public String productoGuardar(Producto producto,
            @RequestParam("imagenFile") MultipartFile imagenFile) {        
        if (!imagenFile.isEmpty()) {
            productoService.save(producto);
            producto.setRutaImagen(
                    firebaseStorageService.cargaImagen(
                            imagenFile, 
                            "producto", 
                            producto.getIdProducto()));
        }
        productoService.save(producto);
        return "redirect:/producto/listado";
    }

    @GetMapping("/eliminar/{idProducto}")
    public String productoEliminar(Producto producto) {
        productoService.delete(producto);
        return "redirect:/producto/listado";
    }

@GetMapping("/modificar/{idProducto}")
public String productoModificar(Producto producto, Model model) {
    producto = productoService.getProducto(producto);
    var categorias = categoriaService.getCategorias(false); // <---  pasar la lista de categorias al modelo
    model.addAttribute("producto", producto);
    model.addAttribute("categorias", categorias); // <---  pasar la lista de categorías al modelo
    return "/producto/modifica";
}

}
