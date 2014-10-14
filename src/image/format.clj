(ns image.format
  (:import [java.awt.image BufferedImage]))

(def image-format
  {:BMP "BMP"
   :jpeg "jpeg"
   :bmp "bmp"
   :wbmp "wbmp"
   :gif "gif"
   :JPG "JPG"
   :png "png"
   :jpg "jpg"
   :JPEG "JPEG"
   :WBMP "WBMP"})

(def image-type
  {:3-byte-bgr BufferedImage/TYPE_3BYTE_BGR
   :4-byte-abgr BufferedImage/TYPE_4BYTE_ABGR
   :4-byte-abgr-pre BufferedImage/TYPE_4BYTE_ABGR_PRE
   :byte-binary BufferedImage/TYPE_BYTE_BINARY
   :byte-gray BufferedImage/TYPE_BYTE_GRAY
   :byte-indexed BufferedImage/TYPE_BYTE_INDEXED
   :custom BufferedImage/TYPE_CUSTOM
   :int-argb BufferedImage/TYPE_INT_ARGB
   :int-argb-pre BufferedImage/TYPE_INT_ARGB_PRE
   :int-bgr BufferedImage/TYPE_INT_BGR
   :int-rgb BufferedImage/TYPE_INT_RGB
   :ushort-555-rgb BufferedImage/TYPE_USHORT_555_RGB
   :ushort-565-rgb BufferedImage/TYPE_USHORT_565_RGB
   :ushort-gray BufferedImage/TYPE_USHORT_GRAY})

(def idx->image-type
  {BufferedImage/TYPE_3BYTE_BGR :3-byte-bgr 
   BufferedImage/TYPE_4BYTE_ABGR :4-byte-abgr 
   BufferedImage/TYPE_4BYTE_ABGR_PRE :4-byte-abgr-pre 
   BufferedImage/TYPE_BYTE_BINARY :byte-binary 
   BufferedImage/TYPE_BYTE_GRAY :byte-gray 
   BufferedImage/TYPE_BYTE_INDEXED :byte-indexed 
   BufferedImage/TYPE_CUSTOM :custom 
   BufferedImage/TYPE_INT_ARGB :int-argb 
   BufferedImage/TYPE_INT_ARGB_PRE :int-argb-pre 
   BufferedImage/TYPE_INT_BGR :int-bgr 
   BufferedImage/TYPE_INT_RGB :int-rgb 
   BufferedImage/TYPE_USHORT_555_RGB :ushort-555-rgb 
   BufferedImage/TYPE_USHORT_565_RGB :ushort-565-rgb 
   BufferedImage/TYPE_USHORT_GRAY :ushort-gray})
