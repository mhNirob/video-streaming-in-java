package org.nirob;


import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nirob
 */
public class Packet implements Externalizable {
    //private int play;
    private byte[] data;

    public Packet() {
        
    }
    public Packet(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            data = baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void writeExternal(ObjectOutput oo) throws IOException {
        oo.writeInt(data.length);
        oo.write(data);
    }

    @Override
    public void readExternal(ObjectInput oi) throws IOException, ClassNotFoundException {
        data = new byte[oi.readInt()];
        oi.readFully(data);
    }

    public BufferedImage getImage() {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            return ImageIO.read(bais);
        } catch (IOException ex) {
            Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
