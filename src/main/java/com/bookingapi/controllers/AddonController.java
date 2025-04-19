package com.bookingapi.controllers;

import com.bookingapi.models.Addon;
import com.bookingapi.services.AddonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addons")
public class AddonController {

    @Autowired
    private AddonService addonService;

    // Crear un nuevo addon (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Addon createAddon(@RequestParam String name, @RequestParam Double price, @RequestParam(required = false) String description) {
        return addonService.createAddon(name, price, description);
    }

    // Obtener todos los addons (cualquier usuario)
    @GetMapping
    public List<Addon> getAllAddons() {
        return addonService.getAllAddons();
    }

    // Obtener un addon por ID (cualquier usuario)
    @GetMapping("/{id}")
    public Addon getAddonById(@PathVariable Long id) {
        return addonService.getAddonById(id);
    }

    // Obtener un addon por nombre (cualquier usuario)
    @GetMapping("/name/{name}")
    public Addon getAddonByName(@PathVariable String name) {
        return addonService.getAddonByName(name);
    }

    // Actualizar un addon (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Addon updateAddon(@PathVariable Long id, @RequestParam String name, @RequestParam Double price, @RequestParam(required = false) String description) {
        return addonService.updateAddon(id, name, price, description);
    }

    // Eliminar un addon (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAddon(@PathVariable Long id) {
        addonService.deleteAddon(id);
    }
}
