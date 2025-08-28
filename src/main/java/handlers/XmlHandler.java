package handlers;

import model.Monster;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlHandler extends AbstractHandler {
    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }

    @Override
    protected List<Monster> processFile(File file) {
        List<Monster> monsters = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new FileReader(file)));
            
            NodeList monsterNodes = document.getElementsByTagName("monster");
            for (int i = 0; i < monsterNodes.getLength(); i++) {
                Element monsterElement = (Element) monsterNodes.item(i);
                Monster monster = parseXmlMonster(monsterElement);
                monster.setSource("XML: " + file.getName());
                monsters.add(monster);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monsters;
    }

    private Monster parseXmlMonster(Element monsterElement) {
        Monster monster = new Monster();
        monster.setName(monsterElement.getElementsByTagName("name").item(0).getTextContent());
        monster.setDescription(monsterElement.getElementsByTagName("description").item(0).getTextContent());
        monster.setDangerLevel(Integer.parseInt(monsterElement.getElementsByTagName("dangerLevel").item(0).getTextContent()));
        
        NodeList habitats = monsterElement.getElementsByTagName("habitats");
        for (int j = 0; j < habitats.getLength(); j++) {
            monster.getHabitats().add(habitats.item(j).getTextContent());
        }
        
        monster.setFirstMention(monsterElement.getElementsByTagName("firstMention").item(0).getTextContent());
        
        NodeList immunities = monsterElement.getElementsByTagName("immunities");
        for (int j = 0; j < immunities.getLength(); j++) {
            monster.getImmunities().add(immunities.item(j).getTextContent());
        }
        
        monster.setActivityTime(monsterElement.getElementsByTagName("activityTime").item(0).getTextContent());
        monster.setPoisonRecipe(monsterElement.getElementsByTagName("poisonRecipe").item(0).getTextContent());
        monster.setPreparationTime(Integer.parseInt(monsterElement.getElementsByTagName("preparationTime").item(0).getTextContent()));
        monster.setEffectiveness(monsterElement.getElementsByTagName("effectiveness").item(0).getTextContent());
        monster.setHeight(Double.parseDouble(monsterElement.getElementsByTagName("height").item(0).getTextContent()));
        monster.setWeight(Double.parseDouble(monsterElement.getElementsByTagName("weight").item(0).getTextContent()));
        
        return monster;
    }

    @Override
    protected void writeFile(List<Monster> monsters, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<monsters>\n");
            
            for (Monster monster : monsters) {
                writer.write("  <monster>\n");
                writeXmlElement(writer, "name", monster.getName());
                writeXmlElement(writer, "description", monster.getDescription());
                writeXmlElement(writer, "dangerLevel", String.valueOf(monster.getDangerLevel()));
                
                for (String habitat : monster.getHabitats()) {
                    writeXmlElement(writer, "habitats", habitat);
                }
                
                writeXmlElement(writer, "firstMention", monster.getFirstMention());
                
                for (String immunity : monster.getImmunities()) {
                    writeXmlElement(writer, "immunities", immunity);
                }
                
                writeXmlElement(writer, "activityTime", monster.getActivityTime());
                writeXmlElement(writer, "poisonRecipe", monster.getPoisonRecipe());
                writeXmlElement(writer, "preparationTime", String.valueOf(monster.getPreparationTime()));
                writeXmlElement(writer, "effectiveness", monster.getEffectiveness());
                writeXmlElement(writer, "height", String.valueOf(monster.getHeight()));
                writeXmlElement(writer, "weight", String.valueOf(monster.getWeight()));
                writer.write("  </monster>\n");
            }
            
            writer.write("</monsters>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeXmlElement(FileWriter writer, String tagName, String value) throws IOException{
        writer.write("    <" + tagName + ">" + value + "</" + tagName + ">\n");
    }
}