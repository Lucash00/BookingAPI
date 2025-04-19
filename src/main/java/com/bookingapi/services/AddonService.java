package com.bookingapi.services;

import com.bookingapi.exceptions.ResourceNotFoundException;
import com.bookingapi.models.Addon;
import com.bookingapi.repositories.AddonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddonService {

    @Autowired
    private AddonRepository addonRepository;

    // Crear un nuevo addon
    public Addon createAddon(String name, Double price, String description) {
        Addon addon = new Addon(name, price, description);
        return addonRepository.save(addon);
    }

    // Obtener todos los addons
    public List<Addon> getAllAddons() {
        return addonRepository.findAll();
    }

    // Obtener un addon por ID
    public Addon getAddonById(Long id) {
        return addonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found"));
    }

    // Obtener un addon por nombre
    public Addon getAddonByName(String name) {
        return addonRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Addon not found with name: " + name));
    }

    // Eliminar un addon
    public void deleteAddon(Long id) {
        if (!addonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Addon not found");
        }
        addonRepository.deleteById(id);
    }

    // Actualizar un addon existente
    public Addon updateAddon(Long id, String name, Double price, String description) {
        Addon addon = getAddonById(id);
        addon.setName(name);
        addon.setPrice(price);
        addon.setDescription(description);
        return addonRepository.save(addon);
    }
}
