package snownee.textanimator.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.resources.ResourceLocation;

@Mixin(Font.class)
public interface FontAccess {
	@Accessor
	boolean getFilterFishyGlyphs();

	@Invoker
	FontSet callGetFontSet(ResourceLocation resourceLocation);

	@Invoker
	void callRenderChar(
			BakedGlyph bakedGlyph,
			boolean bl,
			boolean bl2,
			float f,
			float g,
			float h,
			Matrix4f matrix4f,
			VertexConsumer vertexConsumer,
			float i,
			float j,
			float k,
			float l,
			int m);
}
