package me.koshey.hack.client.test;
public class TestLoc {
    public static void main(String[] args) {
        try {
            Class<?> rl = Class.forName("net.minecraft.resources.ResourceLocation");
            System.out.println("Found ResourceLocation");
        } catch (Exception e) {}
        try {
            Class<?> rl = Class.forName("net.minecraft.util.Identifier");
            System.out.println("Found Identifier");
        } catch (Exception e) {}
    }
}
