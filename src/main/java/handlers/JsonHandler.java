package handlers;

import model.Monster;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class JsonHandler extends AbstractHandler {
    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }

    @Override
    protected List<Monster> processFile(File file) {
        List<Monster> monsters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            
            JSONArray jsonArray = new JSONArray(content.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Monster monster = parseJsonMonster(jsonObject);
                monster.setSource("JSON: " + file.getName());
                monsters.add(monster);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monsters;
    }

    private Monster parseJsonMonster(JSONObject jsonObject) {
        Monster monster = new Monster();
        monster.setName(jsonObject.getString("name"));
        monster.setDescription(jsonObject.getString("description"));
        monster.setDangerLevel(jsonObject.getInt("dangerLevel"));
        
        JSONArray habitats = jsonObject.getJSONArray("habitats");
        for (int j = 0; j < habitats.length(); j++) {
            monster.getHabitats().add(habitats.getString(j));
        }
        
        monster.setFirstMention(jsonObject.getString("firstMention"));
        
        JSONArray immunities = jsonObject.getJSONArray("immunities");
        for (int j = 0; j < immunities.length(); j++) {
            monster.getImmunities().add(immunities.getString(j));
        }
        
        monster.setActivityTime(jsonObject.getString("activityTime"));
        monster.setPoisonRecipe(jsonObject.getString("poisonRecipe"));
        monster.setPreparationTime(jsonObject.getInt("preparationTime"));
        monster.setEffectiveness(jsonObject.getString("effectiveness"));
        monster.setHeight(jsonObject.getDouble("height"));
        monster.setWeight(jsonObject.getDouble("weight"));
        
        return monster;
    }

    @Override
    protected void writeFile(List<Monster> monsters, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("[\n");
            for (int i = 0; i < monsters.size(); i++) {
                Monster m = monsters.get(i);
                writer.write("  {\n");
                
                writer.write("    \"name\": \"" + m.getName() + "\",\n");
                writer.write("    \"description\": \"" + m.getDescription() + "\",\n");
                writer.write("    \"dangerLevel\": " + m.getDangerLevel() + ",\n");
                writeList(writer, "habitats", m.getHabitats());
                writer.write("    \"firstMention\": \"" + m.getFirstMention() + "\",\n");
                writeList(writer, "immunities", m.getImmunities()); 
                writer.write("    \"activityTime\": \"" + m.getActivityTime() + "\",\n");
                writer.write("    \"poisonRecipe\": \"" + m.getPoisonRecipe() + "\",\n");
                writer.write("    \"preparationTime\": " + m.getPreparationTime() + ",\n");
                writer.write("    \"effectiveness\": \"" + m.getEffectiveness() + "\",\n");
                writer.write("    \"height\": " + m.getHeight() + ",\n");
                writer.write("    \"weight\": " + m.getWeight() + "\n");
                
                writer.write("  },\n");
            }
            writer.write("]");
        } catch (IOException e) {
            showError("Ошибка сохранения файла", e);
        }
    }

    private void writeList(FileWriter writer, String fieldName, List<String> items) throws IOException{
        writer.write("    \"" + fieldName + "\": [");
        for (int j = 0; j < items.size(); j++) {
            writer.write("\"" + items.get(j) + "\"" + (j < items.size() - 1 ? ", " : ""));
        }
        writer.write("],\n");
    }

    private void showError(String title, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            title + ": " + e.getMessage(), 
            "Ошибка", 
            JOptionPane.ERROR_MESSAGE);
    }
}
