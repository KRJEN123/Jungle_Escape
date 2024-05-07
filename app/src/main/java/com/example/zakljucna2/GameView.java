package com.example.zakljucna2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class GameView extends View {
    private Paint platformPaint;
    private Paint characterPaint;
    private List<platform> platforms;
    private character character;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        platformPaint = new Paint();
        platformPaint.setColor(Color.BLUE); // Color for platforms

        characterPaint = new Paint();
        characterPaint.setColor(Color.RED); // Color for the character
    }

    public void setPlatforms(List<platform> platforms) {
        this.platforms = platforms;
    }

    public void setCharacter(character character) {
        this.character = character;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw platforms
        if (platforms != null) {
            for (platform platform : platforms) {
                canvas.drawRect(
                        platform.getX(),
                        platform.getY(),
                        platform.getX() + platform.getWidth(),
                        platform.getY() + platform.getHeight(),
                        platformPaint
                );
            }
        }

        // Draw character
        if (character != null) {
            canvas.drawRect(
                    character.getX(),
                    character.getY(),
                    character.getX() + character.getWidth(),
                    character.getY() + character.getHeight(),
                    characterPaint
            );
        }
    }
}
