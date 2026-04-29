package me.koshey.hack.client.module;

import me.koshey.hack.client.module.impl.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList<>();
    
    public ModuleManager() {
        register(new HUDModule());
        register(new SprintModule());
        register(new FullbrightModule());
        register(new FlyModule());
        register(new NoFallModule());
        register(new VelocityModule());
        register(new ColorsModule());
    }
    
    private void register(Module module) {
        modules.add(module);
    }
    
    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
    
    public ArrayList<Module> getModules() {
        return modules;
    }
    
    public List<Module> getModulesByCategory(Module.Category category) {
        return modules.stream()
            .filter(m -> m.getCategory() == category)
            .collect(Collectors.toList());
    }
    
    public Module getModuleByName(String name) {
        return modules.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public List<Module> getEnabledModules() {
        return modules.stream()
            .filter(Module::isEnabled)
            .collect(Collectors.toList());
    }
}
