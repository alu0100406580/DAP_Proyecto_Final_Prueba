package ull.es;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        HiperdinoCrawler crawlerHiperdino = new HiperdinoCrawler();
        long hiperdinoTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        MercadonaCrawler crawlerMercadona = new MercadonaCrawler();
        long mercadonaTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        TuTrebolCrawler crawlerTuTrebol = new TuTrebolCrawler();
        long tuTrebolTime = System.currentTimeMillis() - startTime;

        System.out.println("Tiempo de ejecución de TuTrebolCrawler: " + tuTrebolTime / (60 * 1000) + " minutos");
        System.out.println("Tiempo de ejecución de MercadonaCrawler: " + mercadonaTime / (60 * 1000) + " minutos");
        System.out.println("Tiempo de ejecución de HiperdinoCrawler: " + hiperdinoTime / (60 * 1000) + " minutos");
        long totalTime = hiperdinoTime + mercadonaTime + tuTrebolTime;
        System.out.println("Tiempo total de ejecución: " + totalTime / (60 * 1000) + " minutos");
    }
}