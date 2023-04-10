import entities.Financeiro;
import entities.Promotores;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;


//ADICIONAR FUNÇÃO PARA PODER CITAR O PORCENTAGEM DO IR E INSS
//DIFERENCIAR A LIST FINANCEIRO, POIS SDR POSSUI COMISSÕES, GARÇONS TAMBÉM SÃO DIFERENTES
//CONFERIR VALE REFEIÇÃO ANTES DO 33167

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.forLanguageTag("pt-BR"));

        HashMap<String, Double> valor = new HashMap<>();
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
            System.out.println(pendencias[i]);
            String valorPendenciaStr = sc.next();
            try {
                double valorPendencia = Double.parseDouble(valorPendenciaStr.replace(",", "."));
                valor.put(pendencias[i], valorPendencia);
            } catch (NumberFormatException e) {
                System.out.println("Valor Inválido");
                i--;
            }}
        imprimirPendencias(valor, pendencias);
    }
    public static void imprimirPendencias(HashMap<String, Double> valor, String[] pendencias) {
        System.out.println("Folha de pagamento, referente Março/2023");
        for (String pendencia : pendencias) {
            Double preco = valor.get(pendencia);
            if (preco != null && preco > 0) {
                String precoString;
                if (preco.intValue() >= 1000) {
                    precoString = String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", preco);
                } else {
                    precoString = String.format(Locale.forLanguageTag("pt-BR"), "%.2f", preco).replace(".", ",");
                }
                System.out.println(pendencia + precoString);
            }
        }
    }
}