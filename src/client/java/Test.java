import java.lang.reflect.Method;
import net.minecraft.client.gui.screens.Screen;

public class Test {
    public static void main(String[] args) {
        for (Method m : Screen.class.getMethods()) {
            if (m.getName().contains("render") || m.getName().contains("mouse") || m.getName().contains("key")) {
                System.out.println(m);
            }
        }
    }
}