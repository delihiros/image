(ns image.core
  (import [javax.swing JOptionPane ImageIcon]
          [javax.imageio ImageIO]
          [java.io ByteArrayOutputStream ByteArrayInputStream]
          [java.awt.image BufferedImage]))

(defn raw-seq->image
  [raw-seq width height image-type]
  (let [image (new-image width height image-type)]
    (.setDataElements
      (.getWritableTile image 0 0)
      0 0 width height
      (byte-array raw-seq))
    image))

(comment
  (show-image
    (raw-seq->image
      (-> "./resources/delihiros.png"
          load-image
          image->raw-seq
          (raw-seq->pixel-seq :4-byte-abgr)
          (pixel-seq->raw-seq :4-byte-abgr))
      240 240 :4-byte-abgr))

  (show-image
    (raw-seq->image
      (-> "./resources/kireso.jpeg"
          load-image
          image->raw-seq
          (raw-seq->pixel-seq (image-type-> 5))
          (pixel-seq->raw-seq (image-type-> 5)))
      459 459
      (image-type-> 5)))



      (show-image
        (raw-seq->image
          (-> "./resources/spam.jpg"
              load-image
              image->raw-seq
              (raw-seq->pixel-seq (image-type-> 5))
              (pixel-seq->raw-seq (image-type-> 5)))
          416 360 :3-byte-bgr)
        ))
