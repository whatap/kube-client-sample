/*
 *  Copyright 2015 the original author or authors. 
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */
package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;

public class CipherAgent {
	private String SEED = "R@WqEeTxr%RtAySuDQW^ERYUAySuDQW^ERTaZXCVRFhGHJKLvsIOfPASgKoDjQ@WqEeTxr%RtBNM%#";
	private String cipherKey;

	private String getSeed() {
		int n = Math.abs((int) KeyGen.next()) % (SEED.length() - 3);
		return SEED.substring(n, n + 2);
	}

	public CipherAgent(String masterKey) {
		if (StringUtil.isEmpty(masterKey)) {
			this.cipherKey = "A#F&#ED@WS%TG";
		} else {
			this.cipherKey = masterKey;
		}
	}

	private Key genKey(String seed) throws GeneralSecurityException {
		byte[] host = (seed + cipherKey + "@EKA%DC$RF").getBytes();
		byte[] key = new byte[8];
		System.arraycopy(host, 0, key, 0, 8);
		return SecretKeyFactory.getInstance("DES") //
				.generateSecret(new DESKeySpec(key));
	}

	public String decrypt(String encoded) {
		try {
			String seed = encoded.substring(0, 2);
			encoded = encoded.substring(2);
			Key key = genKey(seed);
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cyper = decode64(encoded);
			byte[] decoded = cipher.doFinal(cyper);

			return new DataInputX(decoded).readText();
		} catch (Exception e) {
		}
		return encoded;
	}

	public String encrypt(String plain) {
		try {
			String seed = getSeed();

			Key key = genKey(seed);
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainBytes = padding(new DataOutputX().writeText(plain).toByteArray());
			byte[] encoded = cipher.doFinal(plainBytes);
			return seed + encode64(encoded);
		} catch (Exception e) {
		}
		return plain;
	}

	private byte[] decode64(String encoded) throws Exception {
		return Base64.decode(encoded);
	}

	private String encode64(byte[] encoded) {
		return Base64.encode(encoded);
	}

	private byte[] padding(byte[] src) {
		int destlen = (src.length / 8 + 1) * 8;
		byte[] dest = new byte[destlen];
		System.arraycopy(src, 0, dest, 0, src.length);
		return dest;
	}

	public String decryptIfCipher(String value) {
		if (value == null || value.startsWith("{{") == false || value.endsWith("}}") == false)
			return value;
		return decrypt(value.substring(2, value.length() - 2));
	}

	public String encryptIfPlain(String value) {
		if (value == null || (value.startsWith("{{") && value.endsWith("}}")))
			return value;
		return "{{" + encrypt(value) + "}}";
	}

}
