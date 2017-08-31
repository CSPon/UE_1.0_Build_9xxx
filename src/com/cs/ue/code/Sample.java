package com.cs.ue.code;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.lwjgl.input.Mouse;

import com.cs.ue.UniformEngine;
import com.cs.ue.util.texture.UTexture;
import com.cs.ue.util.world.UWorld;

public class Sample extends UniformEngine
{
	private UTexture textures;
	private UWorld world;
	
	public static void main(String[] args)
	{
		new Sample(args);
	}
	
	public Sample(String[] args)
	{
		init(args);
		setBackgroundColor(0, 0, 0);
		setDepthBuffer(-5, 5);
		create();
		
		textures = new UTexture(); textures.targetTexturePack("lib/tex/", "texturepack", "png");
		textures.loadFont();
		
		world = new UWorld(0, 1);
		world.worldSetting(0.5f, 10, 4, 0.3f);
		
		enableDevMode();
		
		Mouse.setCursorPosition(getScrWidth() / 2, getScrHeight() / 2);
		
		run();
	}
	
	@Override
	public void update()
	{
		if(!world.isWorldCreated())
			world.create();
		
		world.update();
	}

	@Override
	public void logic()
	{
		if(!world.isWorldCreated())
			world.create();
	}

	@Override
	public void deltaLogic()
	{	
	}

	@Override
	public void keyboard()
	{
		while(getInput().hasKeyNext())
		{
			if(getInput().getKeys()[KEY_ESCAPE])
				stop();
			if(getInput().getKeys()[KEY_F11])
				enableDevMode();
			if(getInput().getKeys()[KEY_F10])
				world.applyHeight();
		}
		
		world.keyboard(getInput().getKeyboardX() * getDelta(), getInput().getKeyboardY() * getDelta());
	}

	@Override
	public void mouse()
	{
		if(world.isWorldCreated())
			world.mouse(getInput().getMouseX(), getInput().getMouseY(), getScrWidth(), getScrHeight(), 16);
	}

	@Override
	public void cleanup()
	{
		textures.cleanup();
	}

	@Override
	public void render()
	{	
		//noiseTest(0.05f);
		
		if(world.isWorldCreated())
			world.render(16, getScrWidth(), getScrHeight());
		
		glColor3f(1.f, 1.f, 1.f);
		if(renderDevMode())
			world.renderDevMode(16, getScrWidth(), getScrHeight());
		
		renderPointer(1.0f, 1.0f, 1.0f);
	}
	
	public static void renderString(Object string, int x, int y, int size)
	{
		renderString(UTexture.TEX_FONT_ID, string, x, y, size);
	}
}
