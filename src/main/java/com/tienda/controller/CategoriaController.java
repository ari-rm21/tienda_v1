package com.tienda.controller;

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
@RequestMapping("/categoria")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        return "/categoria/listado";
    }
    
    @GetMapping("/nuevo")
    public String categoriaNuevo(Categoria categoria) {
        return "/categoria/modifica";
    }

    @Autowired
    private FirebaseStorageServiceImpl firebaseStorageService;
    
@PostMapping("/guardar")
public String categoriaGuardar(@ModelAttribute Categoria categoriaForm,
        @RequestParam("imagenFile") MultipartFile imagenFile) {

    Categoria categoria = categoriaForm;

    if (categoria.getIdCategoria() != null) {
        // Recuperar la categoría desde DB para mantener productos asociados
        Categoria categoriaDB = categoriaService.getCategoria(categoria);
        categoriaDB.setDescripcion(categoriaForm.getDescripcion());
        categoriaDB.setActivo(categoriaForm.isActivo());

        categoria = categoriaDB;
    }

    if (!imagenFile.isEmpty()) {
        categoria.setRutaImagen(
            firebaseStorageService.cargaImagen(
                imagenFile, "categoria", categoria.getIdCategoria()
            )
        );
    }

    categoriaService.save(categoria);
    return "redirect:/categoria/listado";
}



    @GetMapping("/eliminar/{idCategoria}")
    public String categoriaEliminar(Categoria categoria) {
        categoriaService.delete(categoria);
        return "redirect:/categoria/listado";
    }

    @GetMapping("/modificar/{idCategoria}")
    public String categoriaModificar(Categoria categoria, Model model) {
        categoria = categoriaService.getCategoria(categoria);
        model.addAttribute("categoria", categoria);
        return "/categoria/modifica";
    }   
}
