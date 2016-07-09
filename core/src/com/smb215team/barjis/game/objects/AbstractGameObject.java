/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb215team.barjis.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author dinosaadeh
 */
public abstract class AbstractGameObject {
    private static final String TAG = Dices.class.getName();

    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    /**
     * This is the object's current speed in m/s
     */
    public Vector2 velocity;
    /**
     * This is the object's positive and negative maximum speed in m/s.
     */
    public Vector2 terminalVelocity;
    /**
     * This is an opposing force that slows down the object until 
     * its velocity equals zero. This value is given as a coefficient that is dimensionless. 
     * A value of zero means no friction, and thus the object's velocity will not decrease.
     */
    public Vector2 friction;
    /**
     * This is the object's constant acceleration in m/s².
     */
    public Vector2 acceleration;
    /**
     * The object's bounding box describes the physical body that will be
     * used for collision detection with other objects. The bounding box can be set
     * to any size and is completely independent of the actual dimension of the
     * object in the game world.
     */
    public Rectangle bounds;

    public AbstractGameObject() {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }

    public void update(float deltaTime) {
        updateMotionX(deltaTime);
        updateMotionY(deltaTime);
        // Move to new position
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    public abstract void render(SpriteBatch batch);

    protected void updateMotionX(float deltaTime) {
        if (velocity.x != 0) {
            // Apply friction
            if (velocity.x > 0) {
                velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
            } else {
                velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.x += acceleration.x * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
        
        //Dino: updating the bounds of the sprite
        this.bounds.set(this.position.x, this.position.y, this.dimension.x, this.dimension.y);
    }

    protected void updateMotionY(float deltaTime) {
        if (velocity.y != 0) {
            // Apply friction
            if (velocity.y > 0) {
                velocity.y = Math.max(velocity.y - friction.y
                        * deltaTime, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y
                        * deltaTime, 0);
            }
        }
        // Apply acceleration
        velocity.y += acceleration.y * deltaTime;
        // Make sure the object's velocity does not exceed the
        // positive or negative terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
        
        //Dino: updating the bounds of the sprite
        this.bounds.set(this.position.x, this.position.y, this.dimension.x, this.dimension.y);
    }
}