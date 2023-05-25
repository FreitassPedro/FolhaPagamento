import entities.Financeiro;
import entities.Promotores;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

        LinkedHashMap<String, Double> valor = new LinkedHashMap<>();

        char escolha;
        //Pergunta qual dos grupos deseja!
        do {
            System.out.println("Selecione o grupo:");
            System.out.println("Financeiro (f) / Promotor (p)");
            escolha = sc.next().toUpperCase() .charAt(0);
        } while (escolha != 'F' && escolha != 'P' );

        // seleciona as pendências correspondentes ao grupo escolhido
        String[] pendencias = escolha == 'F' ? Financeiro.PENDENCIAS : Promotores.PENDENCIAS;

        // Pergunta o valor para cada pendência e adiciona na lista;
        for (int i = 0; i < pendencias.length; i++) {
            String pergunta = pendencias[i];

            // pergunta pela porcentagem de INSS ou IR, caso seja uma dessas pendências
                if (pendencias[i].equals( "(-)INSS %s R$ ") || pendencias[i].equals("(-)IR %s R$ ")) {
                    System.out.println("Porcentagem INSS/IR: ");
                    String porcentagem = sc.next();
                    pergunta = String.format(pergunta, porcentagem + "%");
                }

            System.out.println(pergunta);
            String valorPendenciaStr = sc.next();

            if (valorPendenciaStr.equalsIgnoreCase("b") ) {
                System.out.println("Voltando!");
                i--; //Volta uma pendência
            }
            else {
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
        imprimirPendencias(valor);
    }

    //IDENTIFICA O MÊS ANTERIOR DO USUÁRIO
    public static String dataAtual() {
        LocalDate currentDate = LocalDate.now().minusMonths(1);
        int currentYear = LocalDate.now().getYear();
        Locale.setDefault(new Locale("pt", "BR"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault());
        String previousMonth = currentDate.format(formatter);
        previousMonth = Character.toUpperCase(previousMonth.charAt(0)) + previousMonth.substring(1);

        System.out.println(previousMonth);

        return previousMonth + "/" + currentYear;
    }

    //IMPRIME TUDO QUE FOI DIGITADO NO LOOP FOR
    public static void imprimirPendencias(HashMap<String, Double> valor) throws IOException {
        System.out.println();
        System.out.println();
        FileWriter fileWriter = new FileWriter("arquivo.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String tituloFolha = "Folha de pagamento, referente " + dataAtual();

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
}
