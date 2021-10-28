package com.discordbot.babybot.profile_card;

import com.discordbot.babybot.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileCard {
    static Graphics2D g;
    public File draw(int cardWidth, User user, Guild guild) throws IOException {

        Member member = guild.getMember(user);
        String username = member.getUser().getName();
        List<String> roles = member.getRoles().stream().map(Role::getName).toList();
        String nickname = member.getNickname();
        Color color = member.getColor();
        String avatarUrl = member.getAvatarUrl();

        if (avatarUrl == null) avatarUrl = member.getUser().getAvatarUrl();
        if (color == null)  color = Utils.randomColor();

        HashMap<String, String> info = new HashMap<>();
        info.put("Username", username);
        if(!roles.isEmpty()) info.put("Role", roles.toString());
        if(nickname != null) info.put("Nickname", nickname);

        float aspectRatioWidth = (float) 9 / 21; float aspectRatioHeight = (float) 21 / 9;

        Font font = new Font("Calibri", Font.BOLD, 20);
        BufferedImage profileCard = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        g = profileCard.createGraphics();
        FontMetrics metrics = g.getFontMetrics(font);

        String topText = info.get("Role") + info.get("Nickname");

        int padding = 40;

        int width = metrics.stringWidth(topText) + padding * 2;
        int height = (int) (metrics.getHeight() * (info.size() + 0.5));
        int cardHeight = (int) (cardWidth * aspectRatioWidth);
        int avatarSize = cardHeight - padding * 2;

        if (width + avatarSize > cardWidth | height > cardHeight) {
            if (width > height) {
                cardWidth = (int) (width * 1.5);
                cardHeight = (int) (cardWidth * aspectRatioWidth);
            } else {
                cardHeight = (int) (height * 1.5);
                cardWidth = (int) (cardHeight * aspectRatioHeight);
            }
            avatarSize = cardHeight - padding * 2;
        }

        profileCard = new BufferedImage(cardWidth, cardHeight, BufferedImage.TYPE_INT_ARGB);
        g = profileCard.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawRectangle(0, 0, cardWidth, cardHeight, color);
        drawInfo(info, font, cardWidth, cardHeight, padding);

        BufferedImage avatarImage = ImageIO.read(new URL(avatarUrl));
        drawAvatar(padding / 2, (cardHeight - avatarSize) / 2, avatarSize, avatarImage);

        return cardToInputStream(profileCard);
    }

    public static void drawInfo(HashMap<String, String> info, Font font, int cardWidth, int cardHeight, int padding) {
        FontMetrics metrics = g.getFontMetrics(font);
        for (Map.Entry<String, String> i : info.entrySet()) {
            switch (i.getKey()) {
                case "Role" -> {
                    drawRectangle(Math.abs(padding / 2 - cardWidth) - metrics.stringWidth(i.getKey() + ": " + i.getValue()),
                            ((metrics.getHeight() - font.getSize() + font.getSize() / 10)),
                            metrics.stringWidth(i.getKey() + ": " + i.getValue()) + 10,
                            metrics.getHeight(),
                            Color.red);
                    g.setColor(Color.WHITE);
                    g.setFont(font);
                    g.drawString(i.getKey() + ": " + i.getValue(),
                            5 + Math.abs(padding / 2 - cardWidth) - metrics.stringWidth(i.getKey() + ": " + i.getValue()),
                            metrics.getHeight());
                }
                case "Username" -> {
                    g.setColor(Color.WHITE);
                    g.setFont(font);
                    g.drawString(i.getKey() + ": " + i.getValue(),
                            (float) (padding / 2),
                            (float) (cardHeight - metrics.getHeight() + padding / 2.5));
                }
                case "Nickname" -> {
                    g.setColor(Color.WHITE);
                    g.setFont(font);
                    g.drawString(i.getKey() + ": " + i.getValue(),
                            (float) (padding / 2),
                            metrics.getHeight());
                }
            }
        }
    }

    public File cardToInputStream(BufferedImage image) {
        try {
            File outputFile = new File("card.png");
            ImageIO.write(image, "png", outputFile);
            return outputFile;
        } catch (IOException ex) {
            return null;
        }
    }

    public static void drawRectangle(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        int arc = height / 15;
        if (arc < 10) {
            arc = 10;
        }
        g.drawRoundRect(x, y, width, height, arc, arc);
        g.fillRoundRect(x, y, width, height, arc, arc);
    }

    public static void drawAvatar(int x, int y, int size, BufferedImage image) {
        Rectangle r = new Rectangle(x, y, size, size);
        TexturePaint tp = new TexturePaint(image, r);
        g.setPaint(tp);
        g.drawOval(x, y, size, size);
        g.fillOval(x, y, size, size);
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(2));
        g.drawOval(x, y, size, size);
    }
}
