package com.discordbot.babybot.profile_card;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileCard {
    static Graphics2D g;
    static int textCounter = 1;

    private final Logger log = LoggerFactory.getLogger(ProfileCard.class);

    public File draw(int cardWidth, User user, Guild guild) {
        Member member = guild.getMember(user);
        if (member != null) {
            textCounter = 1;
            int cardHeight = (int) (cardWidth / 2.5);
            int avatarSize = cardHeight - cardWidth / 12;
            int avatarPos = (cardHeight - avatarSize) / 2;
            int fontSize = cardHeight / 10;
            int textPosX = (int) (cardWidth * 1.4);
            int rolePosX = (int) (cardWidth * 1.7); //1.4
            int maxWidth = (cardWidth / 2);

            Color cardColor = null;
            String nickname = member.getNickname();
            String username = user.getName();
            String roleName = null;
            BufferedImage avatarImage = null;
            List<Role> roles = member.getRoles();
            if (!roles.isEmpty()) {
                for (Role role : roles) {
                    if (role.getColor() != null) {
                        cardColor = role.getColor();
                        roleName = role.getName();
                        break;
                    }
                }
            } else {
                cardColor = Color.GRAY;
            }
            try {
                avatarImage = ImageIO.read(new URL(Objects.requireNonNull(user.getAvatarUrl())));
            } catch (IOException ex) {
                log.error("Failed to obtain image from AvatarUrl", ex);
            }
            BufferedImage profileCard = new BufferedImage(cardWidth, cardHeight, BufferedImage.TYPE_INT_ARGB);
            g = profileCard.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            drawRectangle(0, 0, cardWidth, cardHeight, cardColor); //the card
            if (avatarImage != null) {
                drawAvatar(avatarPos, avatarPos, avatarSize, avatarSize, avatarImage);
            }
            if (roleName != null) {
                text(rolePosX, Color.white, "ROLE: " + roleName, fontSize, maxWidth);
            }
            text(textPosX, Color.white, "USERNAME: " + username, fontSize, maxWidth);
            if (nickname != null) {
                text(textPosX, Color.white, "A.K.A: " + nickname, fontSize, maxWidth);
            }
            //ImageIO.write(profileCard, "PNG", new File("src/main/resources/templates/" + username + "profile.png"));

            return cardToInputStream(profileCard);
        } else {
            log.error("Member is empty, can't generate PROFILE CARD");
            return null;
        }
    }

    public File cardToInputStream(BufferedImage image) {
        try {
            File outputFile = new File("card.png");
            ImageIO.write(image, "png", outputFile);
            return outputFile;
        } catch (IOException ex) {
            log.error("Failed to convert PROFILE CARD BUFFERED IMAGE TO FILE", ex);
            return null;
        }
    }

    public static void text(int x, Color color, String text, int fontSize, int maxWidth) {
        Font font = new Font("Calibri", Font.BOLD, fontSize);

        FontMetrics metrics = g.getFontMetrics(font);
        int y = metrics.getHeight() * textCounter;


        if (metrics.stringWidth(text) > maxWidth) {
            List<String> array = new ArrayList<>();
            array.add(text.substring(0, text.indexOf(" ", text.length() / 2)));
            array.add(text.substring(text.indexOf(" ", text.length() / 2)));
            for (String word : array) {
                text(x, color, word, fontSize, maxWidth);
            }
        } else {
            x = x + (-x - metrics.stringWidth(text)) / 2;
            g.setColor(color);
            g.setFont(font);
            g.drawString(text, x, y);
            textCounter++;
        }
    }


    public static void drawLine(int x1, int y1, int x2, int y2, int stroke, Color color) {
        g.setColor(color);
        g.setStroke(new BasicStroke(stroke));
        g.drawLine(x1, y1, x2, y2);
    }

    public static void drawRectangle(int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.drawRoundRect(x, y, width, height, width / 10, width / 10);
        g.fillRoundRect(x, y, width, height, width / 10, width / 10);
    }

    public static void drawAvatar(int x, int y, int width, int height, BufferedImage image) {
        Rectangle r = new Rectangle(x, y, width, height);
        TexturePaint tp = new TexturePaint(image, r);
        g.setPaint(tp);
        g.drawOval(x, y, width, height);
        g.fillOval(x, y, width, height);
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(2));
        g.drawOval(x, y, width, height);
    }


}
