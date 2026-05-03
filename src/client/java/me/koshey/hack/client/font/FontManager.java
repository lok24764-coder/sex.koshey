package me.koshey.hack.client.font;

import java.awt.Font;
import java.io.InputStream;

public class FontManager {
    private CustomFont medium;
    private CustomFont bold;
    private CustomFont small;
    private CustomFont large;

    public FontManager() {
        try {
            InputStream mediumStream = FontManager.class.getResourceAsStream("/assets/koshey/fonts/sf_medium.ttf");
            if (mediumStream == null) {
                System.out.println("Could not find sf_medium.ttf");
            } else {
                Font sfMedium = Font.createFont(Font.TRUETYPE_FONT, mediumStream);
                this.medium = new CustomFont(sfMedium.deriveFont(18f), "medium");
                this.small = new CustomFont(sfMedium.deriveFont(16f), "small");
            }
            
            InputStream boldStream = FontManager.class.getResourceAsStream("/assets/koshey/fonts/sf_bold.ttf");
            if (boldStream == null) {
                System.out.println("Could not find sf_bold.ttf");
            } else {
                Font sfBold = Font.createFont(Font.TRUETYPE_FONT, boldStream);
                this.bold = new CustomFont(sfBold.deriveFont(18f), "bold");
                this.large = new CustomFont(sfBold.deriveFont(24f), "large");
            }
            
            if (this.medium == null) throw new RuntimeException("Fallback to Arial");
        } catch (Exception e) {
            e.printStackTrace();
            Font fallback = new Font("Arial", Font.PLAIN, 18);
            this.medium = new CustomFont(fallback, "medium");
            this.small = new CustomFont(fallback.deriveFont(16f), "small");
            this.bold = new CustomFont(fallback.deriveFont(Font.BOLD, 18f), "bold");
            this.large = new CustomFont(fallback.deriveFont(Font.BOLD, 24f), "large");
        }
    }

    public CustomFont getFont(String name) {
        return medium;
    }

    public CustomFont getMedium() {
        return medium;
    }

    public CustomFont getBold() {
        return bold;
    }

    public CustomFont getSmall() {
        return small;
    }

    public CustomFont getLarge() {
        return large;
    }
}
