import entities.Financeiro;
import entities.Promotores;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.forLanguageTag("pt-BR"));
        LinkedHashMap<String, Double> valor = new LinkedHashMap<>();
        char escolha;
        //Pergunta qual dos grupos deseja!
        do {
            System.out.println("Financeiro (f) / Promotor (p)");
            escolha = sc.next().charAt(0);
        }
        while (escolha != 'F' && escolha != 'f' && escolha != 'P' && escolha != 'p');
        String[] pendencias = escolha == 'F' || escolha == 'f' ? Financeiro.PENDENCIAS : Promotores.PENDENCIAS;

        //Pergunta o valor para o usuário e adiciona a lista
        for (int i = 0; i < pendencias.length; i++) {
            String pergunta = pendencias[i];
                //PERGUNTA VALOR A SER ADICIONADO ANTES DO "%"
                if (pendencias[i].equals( "(-)INSS %s R$ ") || pendencias[i].equals("(-)IR %s R$ ")) {
                    System.out.println("Porcentagem INSS/IR: ");
                    String porcentagem = sc.next();
                    pergunta = String.format(pergunta, porcentagem + "%");
                }

            System.out.println(pergunta);
            String valorPendenciaStr = sc.next();
            try {
                double valorPendencia = Double.parseDouble(valorPendenciaStr.replace(",", "."));
                valor.put(pergunta, valorPendencia);
            } catch (NumberFormatException e) {
                System.out.println("Valor Inválido");
                i--;
            }
        }
        imprimirPendencias(valor);
    }


    public static void imprimirPendencias(HashMap<String, Double> valor) {
        System.out.println("Folha de pagamento, referente Março/2023");
        for (Map.Entry<String, Double> pendencia : valor.entrySet()) {
            Double preco = pendencia.getValue();
            if (preco != null && preco > 0) {
                String precoString;
                if (preco.intValue() >= 1000) {
                    precoString = String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", preco);
                } else {
                    precoString = String.format(Locale.forLanguageTag("pt-BR"), "%.2f", preco).replace(".", ",");
                }
                System.out.println(pendencia.getKey() + precoString);
            }
        }
    }
}