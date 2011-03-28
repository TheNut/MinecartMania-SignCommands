package com.afforess.minecartmaniasigncommands;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.ChatColor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.config.MinecartManiaConfigurationParser;
import com.afforess.minecartmaniacore.config.SettingParser;

public class SignCommandsSettingParser implements SettingParser{
	private static final double version = 1.1;
	
	public boolean isUpToDate(Document document) {
		try {
			NodeList list = document.getElementsByTagName("version");
			Double version = MinecartManiaConfigurationParser.toDouble(list.item(0).getChildNodes().item(0).getNodeValue(), 0);
			return version == SignCommandsSettingParser.version;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean read(Document document) {
		Object value;
		NodeList list;
		String setting;
		
		try {
			setting = "AnnouncementSignPrefix";
			list = document.getElementsByTagName(setting);
			value = list.item(0).getChildNodes().item(0).getNodeValue();
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "AnnouncementSignPrefixColor";
			list = document.getElementsByTagName(setting);
			value = parseColor(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "AnnouncementColor";
			list = document.getElementsByTagName(setting);
			value = parseColor(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "SensorDisabledDelay";
			list = document.getElementsByTagName(setting);
			value = list.item(0).getChildNodes().item(0).getNodeValue();
			MinecartManiaWorld.getConfiguration().put(setting, MinecartManiaConfigurationParser.toInt((String) value, 8));
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean write(File configuration) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//root elements
			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);
			Element rootElement = doc.createElement("MinecartManiaConfiguration");
			doc.appendChild(rootElement);
			
			Element setting = doc.createElement("version");
			setting.appendChild(doc.createTextNode("1.1"));
			rootElement.appendChild(setting);
			
			setting = doc.createElement("AnnouncementSignPrefix");
			Comment comment = doc.createComment("The prefix displayed before all announcement messages are displayed to the player.");
			setting.appendChild(doc.createTextNode("[Announcement]"));
			rootElement.appendChild(setting);
			rootElement.insertBefore(comment,setting);
			
			setting = doc.createElement("AnnouncementSignPrefixColor");
			String colors = "";
			for (ChatColor c : ChatColor.values()) {
				colors += c.name().toLowerCase() + ", "; 
			}
			comment = doc.createComment("The color of the prefix. Valid Colors are: \n\t" + colors);
			setting.appendChild(doc.createTextNode("yellow"));
			rootElement.appendChild(setting);
			rootElement.insertBefore(comment,setting);
			
			setting = doc.createElement("AnnouncementColor");
			comment = doc.createComment("The color of the message of the announcement");
			setting.appendChild(doc.createTextNode("white"));
			rootElement.appendChild(setting);
			rootElement.insertBefore(comment,setting);
			
			
			setting = doc.createElement("SensorDisabledDelay");
			comment = doc.createComment("The delay (in ticks. 20 ticks = 1 second) that sensors stay active after a minecart passes");
			setting.appendChild(doc.createTextNode("8"));
			rootElement.appendChild(setting);
			rootElement.insertBefore(comment,setting);
			
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(configuration);
			transformer.transform(source, result);
		}
		catch (Exception e) { return false; }
		return true;
	}
	
	private static ChatColor parseColor(String str) {
		for (ChatColor c : ChatColor.values()) {
			if (c.name().equalsIgnoreCase(str)) {
				return c;
			}
		}
		return ChatColor.WHITE;
	}
}
