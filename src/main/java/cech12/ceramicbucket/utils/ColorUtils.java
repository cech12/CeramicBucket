package cech12.ceramicbucket.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.*;

public class ColorUtils {

    public static HashMap<Fluid, Integer> colorCache = new HashMap<>();

    public static int getFluidColor(Fluid fluid) {
        Integer color = 0xffffffff;
        if (fluid != null) {
            color = colorCache.get(fluid);
            if (color == null) {
                color = fluid.getAttributes().getColor();
                if (color == 0xffffffff) {
                    color = ColorUtils.getColorFrom(fluid.getAttributes().getStillTexture());
                    if (color == null) {
                        color = 0xffffffff;
                    }
                }
                colorCache.put(fluid, color);
            }
        }
        return color;
    }

    private static Integer getColorFrom(ResourceLocation location) {
        ITextureObject texture = Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        if (texture instanceof AtlasTexture) {
            return getColorFrom(((AtlasTexture) texture).getSprite(location));
        }
        return null;
    }

    private static Integer getColorFrom(TextureAtlasSprite sprite) {
        if (sprite == null) return null;
        if (sprite.getFrameCount() == 0) return null;
        Map<Integer, Integer> colorMap = new HashMap<>();
        for (int x = 0; x < sprite.getWidth(); x++) {
            for (int y = 0; y < sprite.getHeight(); y++) {
                if (!sprite.isPixelTransparent(0, x, y)) {
                    int color = sprite.getPixelRGBA(0, x, y);
                    Color c = new Color(color, true);
                    color = rawColorFromRGB(c.getRed(), c.getGreen(), c.getBlue());
                    Integer counter = colorMap.getOrDefault(color, 0);
                    colorMap.put(color, ++counter);
                }
            }
        }
        if (colorMap.isEmpty()) {
            return null;
        }
        return getMostCommonColor(colorMap);
    }

    private static Integer getMostCommonColor(Map<Integer, Integer> map) {
        LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map.Entry<Integer, Integer> entry = list.get(list.size() - 1);
        return entry.getKey();
    }

    private static int rawColorFromRGB(int red, int green, int blue) {
        int rgb = Math.max(Math.min(0xFF, red), 0);
        rgb = (rgb << 8) + Math.max(Math.min(0xFF, green), 0);
        rgb = (rgb << 8) + Math.max(Math.min(0xFF, blue), 0);
        return rgb;
    }


}
