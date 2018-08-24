package com.aesophor.medievania.world.map;

import com.aesophor.medievania.constant.Constants;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameMap {
    
    private TiledMap tiledMap;
    private int mapWidth;
    private int mapHeight;
    private int mapTileSize;
    
    public GameMap(World world, TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        
        // Extract the properties from the map.
        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
        assert tileWidth == tileHeight;
        mapTileSize = tileWidth;
        
        
        // Create BodyDef and FixtureDef.
        Body body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        // Create rectangular ground bodies/fixtures.
        for (MapObject object : tiledMap.getLayers().get(Constants.GROUND_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
            body = world.createBody(bdef);
            
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rect.getWidth() / 2 / Constants.PPM, rect.getHeight() / 2 / Constants.PPM);
            fdef.shape = polygonShape;
            fdef.filter.categoryBits = Constants.GROUND_BIT;
            body.createFixture(fdef);
            
            polygonShape.dispose();
        }
        
        // Create polylinear ground bodies/fixtures.
        for (MapObject object : tiledMap.getLayers().get(Constants.GROUND_LAYER).getObjects().getByType(PolylineMapObject.class)) {
            float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];
            
            for (int i = 0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
            }
            
            ChainShape chainShape = new ChainShape();
            chainShape.createChain(worldVertices);
            
            // We are drawing the polylines using the coordinates of their vertices,
            // so bdef should be set to zero.
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.setZero();
            body = world.createBody(bdef);
            
            fdef.shape = chainShape;
            fdef.filter.categoryBits = Constants.GROUND_BIT;
            body.createFixture(fdef);
            
            chainShape.dispose();
        }
    }
    
    
    public TiledMap getTiledMap() {
        return tiledMap;
    }
    
    public int getMapWidth() {
        return mapWidth;
    }
    
    public int getMapHeight() {
        return mapHeight;
    }
    
    public int getMapTileSize() {
        return mapTileSize;
    }

}