package com.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.api.util.Base64;

/**
 * RSA签名公共类
 * @author shmily
 */
public class RSAUtil{

    private static Logger       log             = Logger
                                                        .getLogger(RSAUtil.class);
    private static RSAUtil      instance;
    private final static String RSA_ENCRYPT_KEY = "encrypt=";
    private final static String DECRYPT_CONTENT = "id_no,card_no,bind_mob,acct_name,cvv2,vali_date";//加密

    private RSAUtil()
    {

    }

    public static RSAUtil getInstance()
    {
        if (null == instance)
            return new RSAUtil();
        return instance;
    }

    /**
     * 公钥、私钥文件生成
     * @param keyPath：保存文件的路径
     * @param keyFlag：文件名前缀
     */
    @SuppressWarnings("unused")
	private void generateKeyPair(String key_path, String name_prefix)
    {
        java.security.KeyPairGenerator keygen = null;
        try
        {
            keygen = java.security.KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e1)
        {
            log.error(e1.getMessage());
        }
        SecureRandom secrand = new SecureRandom();
        secrand.setSeed("21cn".getBytes()); // 初始化随机产生器
        keygen.initialize(1024, secrand);
        KeyPair keys = keygen.genKeyPair();
        PublicKey pubkey = keys.getPublic();
        PrivateKey prikey = keys.getPrivate();

        String pubKeyStr = Base64.getBASE64(pubkey.getEncoded());
        String priKeyStr = Base64.getBASE64(prikey.getEncoded());
        File file = new File(key_path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            // 保存私钥
            FileOutputStream fos = new FileOutputStream(new File(key_path
                    + name_prefix + "_RSAKey_private.txt"));
            fos.write(priKeyStr.getBytes());
            fos.close();
            // 保存公钥
            fos = new FileOutputStream(new File(key_path + name_prefix
                    + "_RSAKey_public.txt"));
            fos.write(pubKeyStr.getBytes());
            fos.close();
        } catch (IOException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * 读取密钥文件内容
     * @param key_file:文件路径
     * @return
     */
    @SuppressWarnings("unused")
	private static String getKeyContent(String key_file)
    {
        File file = new File(key_file);
        BufferedReader br = null;
        InputStream ins = null;
        StringBuffer sReturnBuf = new StringBuffer();
        try
        {
            ins = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            String readStr = null;
            readStr = br.readLine();
            while (readStr != null)
            {
                sReturnBuf.append(readStr);
                readStr = br.readLine();
            }
        } catch (IOException e)
        {
            return null;
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                    br = null;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (ins != null)
            {
                try
                {
                    ins.close();
                    ins = null;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        }
        return sReturnBuf.toString();
    }

    /**
     * 签名处理
     * @param prikeyvalue：私钥文件
     * @param sign_str：签名源内容
     * @return
     */
    public static String sign(String prikeyvalue, String sign_str)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64
                    .getBytesBASE64(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature
                    .getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(sign_str.getBytes("UTF-8"));
            byte[] signed = signet.sign(); // 对信息的数字签名
            return new String(org.apache.commons.codec.binary.Base64
                    .encodeBase64(signed));
        } catch (java.lang.Exception e)
        {
            log.error("签名失败," + e.getMessage());
        }
        return null;
    }

    /**
     * 签名验证
     * @param pubkeyvalue：公钥
     * @param oid_str：源串
     * @param signed_str：签名结果串
     * @return
     */
    public static boolean checksign(String pubkeyvalue, String oid_str,
            String signed_str)
    {
        try
        {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64
                    .getBytesBASE64(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = Base64.getBytesBASE64(signed_str);// 这是SignatureData输出的数字签名
            java.security.Signature signetcheck = java.security.Signature
                    .getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(oid_str.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (java.lang.Exception e)
        {
            log.error("签名验证异常," + e.getMessage());
        }
        return false;
    }

    /**
     * 公钥加密的方法
     * @param source
     * @param public_key
     * @return
     * @throws Exception
     */
    public static String encrypt(String source, String public_key)
            throws Exception
    {

    	byte[] keyByte = java.util.Base64.getDecoder().decode(public_key);        
        X509EncodedKeySpec x509ek = new X509EncodedKeySpec(keyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509ek);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] sbt = source.getBytes("UTF-8"); // 中文情况下 linux 环境要使用这个
//         byte[] sbt = source.getBytes();//本地测试可以使用这个
        byte[] epByte = cipher.doFinal(sbt);
        return java.util.Base64.getEncoder().encodeToString(epByte);

    }

    /**
     * 私钥解密的方法
     * @param cryptograph
     * @param private_key
     * @return
     * @throws Exception
     */
    public static String decrypt(String cryptograph, String private_key)
            throws Exception
    {

        byte[] keyByte = java.util.Base64.getDecoder().decode(private_key);
        PKCS8EncodedKeySpec s8ek = new PKCS8EncodedKeySpec(keyByte);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(s8ek);

        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] b1 = java.util.Base64.getDecoder().decode(cryptograph);
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 
     * @param reqObj 解密对象
     * @param private_key 解密私钥
     * @param encrypt 加密参数
     * @param tag 加密日志
     * @return
     */
    public static JSONObject rsaDecryptObj(JSONObject reqObj,
            String private_key, String encrypt,String tag)
    {
        if (reqObj == null)
        {
            return null;
        }

        // 指定参数解密
        if (encrypt!=null)
        {
            return rsaSpecifyEncryptObj(reqObj, private_key, tag, encrypt);
        }
        // 遍历参数解密
        return rsaErgodicEncryptObj(reqObj, private_key, tag);

    }

    private static JSONObject rsaSpecifyEncryptObj(JSONObject reqObj,
            String private_key, String tag, String encrypt)
    {
        log.info(tag + "进入商户[" + reqObj.getString("oid_partner") + "]参数["
                + encrypt + "]RSA签名解密");
        List<String> keys = new ArrayList<String>(reqObj.keySet());
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            String dec_value = "";
            String value = (String) reqObj.getString(key);
            if (encrypt.contains(key)&&value!=null&&value.length()>32)
            {
                    try
                    {
                        dec_value = RSAUtil.decrypt(value, private_key);
                        log.info(tag + "解密值：" + key + ":" + dec_value);
                    } catch (Exception e)
                    {
                        log.info(tag + "参数[" + key + "]解密失败，原因" + e.getMessage());
                        dec_value = value;
                    }
                 //更新解密值
                reqObj.put(key, dec_value);
            }
        }
        //放置加密参数
        reqObj.put("encrypt", encrypt);
        return reqObj;
    }

    private static JSONObject rsaErgodicEncryptObj(JSONObject reqObj,
            String private_key, String tag)
    {
        List<String> keys = new ArrayList<String>(reqObj.keySet());
        for (int i = 0; i < keys.size(); i++)
        {
            String key = (String) keys.get(i);
            String value = (String) reqObj.getString(key);
            String dec_value = value;
            boolean flag_Encrypt = false;// 解密标示
            if (value.contains(RSA_ENCRYPT_KEY))
            {
                value = value.replaceAll(RSA_ENCRYPT_KEY, "");
                flag_Encrypt = true;

            }
            if (DECRYPT_CONTENT.contains(key)&&value!=null&&value.length() > 32)
            {
                flag_Encrypt = true;
            }
            //解密标示
            if (flag_Encrypt)
            {
                try
                {
                    dec_value = RSAUtil.decrypt(value, private_key);
                    log.info(tag + "解密值：" + key + ":" + dec_value);
                } catch (Exception e)
                {
                    log.info(tag + "参数[" + key + "]解密失败，原因" + e.getMessage());
                    dec_value = value;
                }
            }
            reqObj.put(key, dec_value);
        }
        return reqObj;
    }

}
