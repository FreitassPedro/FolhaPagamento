import entities.Financeiro;
import entities.Promotores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    //ADICINAR FUNÇÃO DE VOLTAR AO i CASO DIGITAR VALOR ERRADO AO INVÉS DE REINICIAR O PROGRAMA
    public static void main(String[] args) throws IOException {

        Locale.setDefault(new Locale("pt", "BR"));

        //Introdução ao Programa
        System.out.println("Digite o valor de cada pendência que aparecer, caso não existir, digite 0 que o programa não imprimirá!");
        System.out.println("Se digitou um número errado e deseja voltar, digite B.");
        System.out.println();

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite nome do funcionario");
        String nameEmployee = sc.nextLine();

        LinkedHashMap<String, Double> valor = new LinkedHashMap<>();

        char escolha;
        //Pergunta qual dos grupos deseja!
        do {
            System.out.println("Selecione o grupo:");
            System.out.println("Financeiro (f) / Promotor (p)");
            escolha = sc.next().toUpperCase().charAt(0);
        } while (escolha != 'F' && escolha != 'P');

        // seleciona as pendências correspondentes ao grupo escolhido
        String[] pendencias = escolha == 'F' ? Financeiro.PENDENCIAS : Promotores.PENDENCIAS;

        // Pergunta o valor para cada pendência e adiciona na lista;
        for (int i = 0; i < pendencias.length; i++) {
            String pergunta = pendencias[i];

            // pergunta pela porcentagem de INSS ou IR, caso seja uma dessas pendências
            if (pendencias[i].equals("(-)INSS %s R$ ") || pendencias[i].equals("(-)IR %s R$ ")) {
                System.out.println("Porcentagem INSS/IR: ");
                String porcentagem = sc.next();
                pergunta = String.format(pergunta, porcentagem + "%");
            }

            System.out.println(pergunta);
            String valorPendenciaStr = sc.next();

            if (valorPendenciaStr.equalsIgnoreCase("b")) {
                System.out.println("Voltando!");
                i--; //Volta uma pendência
            } else {
                try {
                    double valorPendencia = Double.parseDouble(valorPendenciaStr.replace(",", "."));
                    valor.put(pergunta, valorPendencia);
                } catch (NumberFormatException e) {
                    System.out.println("Valor Inválido");
                    i--;
                }
            }
        }
        sc.close();
        //Chama o método abaixo e imprime tudo
        imprimirPendencias(valor, nameEmployee);
        System.out.println(employeeUpperCase(nameEmployee));
    }

    //IDENTIFICA O DATA ANTERIOR DO USUÁRIO
    public static String dataAnterior() {
        LocalDate currentDate = LocalDate.now().minusMonths(1);
        int currentYear = LocalDate.now().getYear();
        Locale.setDefault(new Locale("pt", "BR"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault());
        String previousMonth = currentDate.format(formatter);
        previousMonth = Character.toUpperCase(previousMonth.charAt(0)) + previousMonth.substring(1);


        return previousMonth + "/" + currentYear;
    }

    //IDENTIFICA O MÊS ANTERIOR DO PC DO USUÁRIO
    public static String dataSave() {
        String localData = dataAnterior();
        String[] partes = localData.split("/");

        return partes[0];
    }

    //IMPRIME TUDO QUE FOI DIGITADO NO LOOP FOR
    public static void imprimirPendencias(HashMap<String, Double> valor, String nameEmployee) throws IOException {
        System.out.println();
        System.out.println();
        criadorRepositorio(nameEmployee);
        dataSave();

        String path = pathLocale(employeeUpperCase(nameEmployee))
                + "\\Folha.txt";
        FileWriter fileWriter = new FileWriter(path);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String tituloFolha = "Folha de pagamento, referente " + dataAnterior();

        System.out.println(tituloFolha);
        bufferedWriter.write(tituloFolha);
        bufferedWriter.newLine();
        for (Map.Entry<String, Double> pendencia : valor.entrySet()) {
            Double preco = pendencia.getValue();
            if (preco != null && preco > 0) {
                String precoString;
                if (preco.intValue() >= 1000) {
                    precoString = String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", preco);
                } else {
                    precoString = String.format(Locale.forLanguageTag("pt-BR"), "%.2f", preco).replace(".", ",");
                }
                String saida = pendencia.getKey() + precoString;
                bufferedWriter.write(saida);
                bufferedWriter.newLine();
                System.out.println(saida);
            }
        }
        bufferedWriter.close();
    }


    //MOSTRA O CAMINHO PARA SALVAR
    public static String pathLocale(String nameEmployee) {
        return ("C:\\Users\\CALL1\\Desktop\\SaidaFolha\\"
                + dataSave()
                + "\\"
                + employeeUpperCase(nameEmployee)
                + "\\");
    }

    //CRIA UM DIRETÓRIO NO WINDOWS DO USUÁRIO PARA SALVAR O TXT
    public static void criadorRepositorio(String nameEmployee) {
        Path directoryPath = Paths.get(pathLocale(employeeUpperCase(nameEmployee)));

        // Verifica se o diretório já existe
        if (Files.exists(directoryPath)) {
            System.out.println("Directory already exists: " + directoryPath);
        } else {
            try {
                // Cria o diretório
                Files.createDirectories(directoryPath);
                System.out.println("Directory created: " + directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String employeeUpperCase(String nameEmployee) {
        String[] palavras = nameEmployee.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < palavras.length; i++) {
            String palavra = palavras[i];

            // Converta a primeira letra para maiúscula
            palavra = palavra.substring(0, 1).toUpperCase() + palavra.substring(1);

            result.append(palavra);

            // Adicione um espaço após cada palavra, exceto a última
            if (i < palavras.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
}
