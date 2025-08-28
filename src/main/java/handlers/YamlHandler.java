package handlers;

import model.Monster;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class YamlHandler extends AbstractHandler {
    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".yaml") || file.getName().toLowerCase().endsWith(".yml");
    }

@Override
protected List<Monster> processFile(File file) {
    List<Monster> monsters = new ArrayList<>();
    try {
        Yaml yaml = new Yaml();
        Object loadedData = yaml.load(new FileReader(file));
        
            List<Map<String, Object>> monsterList = (List<Map<String, Object>>) loadedData;
            for (Map<String, Object> monsterData : monsterList) {
                monsters.add(parseYamlMonster(monsterData));
            }
        for (Monster m : monsters) {
            m.setSource("YAML: " + file.getName());
        }     
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
            null, 
            "Ошибка загрузки YAML: " + e.getMessage(), 
            "Ошибка", 
            JOptionPane.ERROR_MESSAGE
        );
    }
    return monsters;
}

    private Monster parseYamlMonster(Map<String, Object> yamlMap) {
        Monster monster = new Monster();
        monster.setName((String) yamlMap.get("name"));
        monster.setDescription((String) yamlMap.get("description"));
        monster.setDangerLevel((Integer) yamlMap.get("dangerLevel"));
        
        List<String> habitats = (List<String>) yamlMap.get("habitats");
        if (habitats != null) {
            monster.setHabitats(habitats);
        }
        
        monster.setFirstMention((String) yamlMap.get("firstMention"));
        
        List<String> immunities = (List<String>) yamlMap.get("immunities");
        if (immunities != null) {
            monster.setImmunities(immunities);
        }
        
        monster.setActivityTime((String) yamlMap.get("activityTime"));
        monster.setPoisonRecipe((String) yamlMap.get("poisonRecipe"));
        monster.setPreparationTime((Integer) yamlMap.get("preparationTime"));
        monster.setEffectiveness((String) yamlMap.get("effectiveness"));
        
        Object height = yamlMap.get("height");
        monster.setHeight(height instanceof Integer ? ((Integer) height).doubleValue() : (Double) height);
        
        Object weight = yamlMap.get("weight");
        monster.setWeight(weight instanceof Integer ? ((Integer) weight).doubleValue() : (Double) weight);
        
        return monster;
    }

   @Override
    protected void writeFile(List<Monster> monsters, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            Yaml yaml = new Yaml();
            for (Monster m : monsters) {
                Map<String, Object> data = new LinkedHashMap<>();
                
                data.put("name", m.getName());
                data.put("description", m.getDescription());
                data.put("dangerLevel", m.getDangerLevel());
                data.put("habitats", m.getHabitats());
                data.put("firstMention", m.getFirstMention());
                data.put("immunities", m.getImmunities());
                data.put("activityTime", m.getActivityTime());
                data.put("poisonRecipe", m.getPoisonRecipe());
                data.put("preparationTime", m.getPreparationTime());
                data.put("effectiveness", m.getEffectiveness());
                data.put("height", m.getHeight());
                data.put("weight", m.getWeight());

                writer.write("-" + yaml.dump(data).replaceAll("(?m)^", "  "));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Ошибка сохранения YAML: " + e.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}