import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
/* Verilen csv dosyasından başlangıç noktası, genişlik ve yükseklik verilerini kullanarak bir dikdörtgenin
 * x başlangıç noktasını, x bitiş noktasını ve yüksekliğini tutan ayrı ayrı dikdörtgenlere ayırır ve bu dikdörtgenleri
 * dikdörtgenler ArrayList'inde tutar.2 boyutlu 2 satır ve x ekseninin uzunluğu kadar sütundan oluşan bir arrayin tüm değerlerine
 * 0 atanır. İlk satır  x noktasına gelene ve o anki yüksekliğe gelene kadar kullanılan ipi, ikinci satır ise x noktasındaki yüksekliğimizi
 * tutar. Herbir dikdörtgen için o dikdörtgenin başlangıç noktasına kadar kullanılan ipe baktığımız dikdörtgenin çevresini ekleyerek giden ve
 * en büyük ip uzunluğunu bulmamaızı sağlayan bir yapı vardır.
 * */ 

public class dynamicProgramming {
    static int genislik = 0; //
    //Bir dikdörtgenin x başlangıç noktasını, x bitiş noktasını ve yüksekliğini tutan Class
    public static class Dikdortgen {
        public final int xStart, xEnd, height;
        public Dikdortgen(final int xStart, final int xEnd, final int height) {
            this.xStart = xStart;
            this.xEnd = xEnd;
            this.height = height;
        }
    }
    //Dikdörtgenleri x başlangıç noktalarına göre sıralayan fonksiyon
    //Çalışma Zamanı:
    //  Insertion Sort algoritması çalışma zamanı n^2 olduğu için ==> O(n^2)
    static void siralama(final ArrayList<Dikdortgen> dikdortgenler) {
        for (int i = 1; i < dikdortgenler.size(); ++i) {
            final Dikdortgen temp = dikdortgenler.get(i);
            int j = i - 1;
            while (j >= 0 && dikdortgenler.get(j).xStart >= temp.xStart) {
                dikdortgenler.set(j + 1, dikdortgenler.get(j));
                j = j - 1;
            }
            dikdortgenler.set(j + 1, temp);
        }
    }
    //CSV Dosyasından Okuyup Onları Dikdörtgen olarak kaydeden fonksiyon
    //Çalışma Zamanı olarak while döngüsünde Satır Satır ayırdığı için n adet dikdörtgen var ise 
    //while döngüsü n kere, içerideki for döngüsü ise ',' e göre split edilen dizinin herbir elemanını
    //aldığı için 3 kere döner. ÇALIŞMA ZAMANI: n * 3 = 3n ==> O(n)'dir.
    static void dosyadanOku(final ArrayList<Dikdortgen> dikdortgenler) {
        try {
            final File inputFile = new File("degerler.csv");
            final FileReader fileReader = new FileReader(inputFile);
            final BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                final String str[] = line.split(",", 3);
                final int b[] = new int[3];
                int i = 0;
                for (final String a : str) {
                    b[i] = Integer.parseInt(a);
                    i++;
                }
                if((b[0] + b[2]) > genislik)
                    genislik = b[0] + b[2];
                dikdortgenler.add(new Dikdortgen(b[2], b[2] + b[0], b[1]));
            }
            reader.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        genislik +=1;
    }

    public static void main(final String[] args) {

        final ArrayList<Dikdortgen> dikdortgenler = new ArrayList<Dikdortgen>();
        dosyadanOku(dikdortgenler);
        siralama(dikdortgenler);
        final int dizi[][] = new int[2][genislik];
     
        /* İlk For n kere döner
         * ikinci for o dikdörtgenin genişliği kadar döner. Max genişlik x ekseninin uzunluğu kadar olabilir.
         * while ise 2 dikdörtgen arasında boşluk var ise boşluk uzubnluğu kadar döner
         * x eksenin genişliğini n cinsinden ifade edemediğim için analizini yapamıyorum.
         * yaklaşık olarak ==> O(n^2)*/
        for(int i = 0; i < dikdortgenler.size(); ++i){
            for(int j = dikdortgenler.get(i).xStart; j <= dikdortgenler.get(i).xEnd; ++j){
                int sayac = 0; 
                if(dikdortgenler.get(i).height >= dizi[1][j] || dikdortgenler.get(i - 1).xEnd == j){
                    while(j > 0 && dizi[0][j - 1] == 0 &&  i != 0){
                        dizi[0][j - 1] = dizi[0][j - 2] + 1;
                        j--;
                        sayac++;
                    }
                    for(int k = j; k < j+sayac; ++k){
                        dizi[0][k] = dizi[0][k - 1] + 1;
                    }
                    j += sayac;
                    if(j == dikdortgenler.get(i).xStart){
                        if(j == 0){
                            dizi[0][j] += Math.abs(dikdortgenler.get(i).height - dizi[1][j]);
                            dizi[1][j] = dikdortgenler.get(i).height;
                        }else{
                            dizi[0][j] = dizi[0][j-1] + Math.abs(dikdortgenler.get(i).height - dizi[1][j]) + 1;
                            dizi[1][j] = dikdortgenler.get(i).height;
                        }
                    }else if(j == dikdortgenler.get(i).xEnd){
                        dizi[0][j] = dizi[0][j - 1] + Math.abs(dikdortgenler.get(i).height - dizi[1][j]) + 1;
                        dizi[1][j] = dikdortgenler.get(i).height;
                    }else{
                        dizi[0][j] = dizi[0][j-1] + 1 + Math.abs(dikdortgenler.get(i).height - dizi[1][j-1]);
                        dizi[1][j] = dikdortgenler.get(i).height;
                    }
                }
            }
        }
        System.out.println("ip uzunlugu: " + dizi[0][genislik - 1]);
        
	}
}
