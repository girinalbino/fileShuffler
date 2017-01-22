package fileShuffler;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class main {

	public static void main(String[] args) {
		String folderName = "D:" + File.separator + "projetos" + File.separator + "workspaces" + File.separator
				+ "fileScrabbler" + File.separator + "teste";

		if (args != null && args[0] instanceof String) {
			folderName = args[0];
		}

		File folder = new File(folderName);

		if (folder.listFiles() == null) {
			return;
		}

		Integer fileCount = folder.listFiles().length;
		int digitosPrefixo = String.valueOf(fileCount).length();

		List<String> prefixos = new ArrayList<String>();
		for (int i = 0; i < fileCount; i++) {
			prefixos.add(String.format("%0" + digitosPrefixo + "d", i + 1));
		}

		Collections.shuffle(prefixos, new Random(Calendar.getInstance().getTimeInMillis()));

		int i = 0;
		for (File arquivoOriginal : folder.listFiles()) {
			if (arquivoOriginal.isFile()) {
				String nomeOriginal = arquivoOriginal.getName().trim();
				String prefixo = prefixos.get(i);

				int posInicioSufixo = nomeOriginal.lastIndexOf('.');
				if (posInicioSufixo < 0) {
					posInicioSufixo = nomeOriginal.length();
				}

				String sufixo = nomeOriginal.substring(posInicioSufixo, nomeOriginal.length());

				// procura do começo do nome até o sufixo pela primeira letra
				int posInicioNomeMidia = 0;
				for (; posInicioNomeMidia < posInicioSufixo; posInicioNomeMidia++) {
					if (String.valueOf(nomeOriginal.charAt(posInicioNomeMidia)).matches("^[A-Za-z]")) {
						break;
					}
				}

				if (posInicioNomeMidia == posInicioSufixo && nomeOriginal.length() > sufixo.length()) {
					// não encontrou letra, nome do arquivo contem apenas
					// números ou caracteres especiais

					if (nomeOriginal.lastIndexOf('-') > 0) {
						posInicioNomeMidia = nomeOriginal.lastIndexOf('-') + 1;
					} else if (nomeOriginal.lastIndexOf('_') > 0) {
						posInicioNomeMidia = nomeOriginal.lastIndexOf('_') + 1;
					} else if (nomeOriginal.lastIndexOf('_') > 0) {
						posInicioNomeMidia = nomeOriginal.lastIndexOf('+') + 1;
					} else if (nomeOriginal.lastIndexOf('+') > 0) {
						posInicioNomeMidia = nomeOriginal.lastIndexOf(' ') + 1;
					} else if (nomeOriginal.indexOf('.') > 0) {
						posInicioNomeMidia = nomeOriginal.indexOf('.') + 1;
					} else {
						// Não achou nenhum caracter especial que denotasse uma
						// separação de prefixo, começa do nome do arquivo...
						posInicioNomeMidia = 0;
					}
				}

				String nomeMidia = nomeOriginal.substring(posInicioNomeMidia, posInicioSufixo).trim();

				String novoNome = prefixo + "-" + nomeMidia + sufixo;

				System.out.println(novoNome);

				File novoArquivo = new File(folderName + File.separator + novoNome);
				arquivoOriginal.renameTo(novoArquivo);

				i++;
			}
		}

		// prefixos.forEach(System.out::println);
	}
}
