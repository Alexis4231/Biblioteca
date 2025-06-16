package Ficheros;

import Biblioteca.Autoria;
import Biblioteca.Libro;

import java.io.*;
import java.util.ArrayList;

public class GestionaFicheros {
    private static File FicheroBin = new File("./files/biblioteca.bin");;

    public GestionaFicheros(){}

    /**
     * Guarda en un fichero pasado por parámetro la información de las colecciones pasadas por parámetro
     * @param f Archivo donde guardar la información
     * @param autorias Colección de autores
     * @param libros Colección de libros
     * @throws IOException En caso de algún error de E/S
     */
    public static void importar(File f, ArrayList<Autoria> autorias, ArrayList<Libro> libros) throws IOException {
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String linea = br.readLine();
        if (linea.equals("***AUT***")) {
            autorias.clear();
            libros.clear();
            int id = -1;
            String nombre = "";
            String apellido = "";
            linea = br.readLine();
            String isbn = "";
            String titulo = "";
            int id_autoria = -1;
            Autoria autoria = null;
            String seccion = "***AUT***";
            while (linea != null) {
                if (linea.equals("***LIB***")) {
                    seccion = "***LIB***";
                }
                if (seccion.equals("***AUT***")) {
                    id = Integer.parseInt(linea);
                    nombre = br.readLine();
                    apellido = br.readLine();
                    autorias.add(new Autoria(id, nombre, apellido));
                    linea = br.readLine();
                } else if (seccion.equals("***LIB***")) {
                    isbn = br.readLine();
                    titulo = br.readLine();
                    id_autoria = Integer.parseInt(br.readLine());
                    for (Autoria a : autorias) {
                        if (a.getId() == id_autoria) {
                            autoria = a;
                        }
                    }
                    libros.add(new Libro(isbn, titulo, autoria));
                    linea = br.readLine();
                }
            }
        }
        br.close();
    }

    /**
     * Guarda en las colecciones de autorías y libros, pasadas por parámetro, la información escrita en el archivo pedido por el usuario
     * @param f Archivo de donde extraer la información
     * @param autorias Colección de autorias
     * @param libros Colección de libros
     * @throws IOException En caso de algún error de E/S
     */
   public static void exportar(File f, ArrayList<Autoria> autorias, ArrayList<Libro> libros) throws IOException {
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        if (autorias.size() != 0) {
            bw.write("***AUT***");
            for (Autoria a : autorias) {
                bw.newLine();
                bw.write(String.valueOf(a.getId()));
                bw.newLine();
                bw.write(a.getNombre());
                bw.newLine();
                bw.write(a.getApellido());
            }
        }
        if (libros.size() != 0) {
            bw.newLine();
            bw.write("***LIB***");
            for (Libro l : libros) {
                bw.newLine();
                bw.write(l.getIsbn());
                bw.newLine();
                bw.write(l.getTitulo());
                bw.newLine();
                bw.write(String.valueOf(l.getAutoria().getId()));
            }
        }
        bw.close();
    }

    /**
     * Guarda en el archivo 'biblioteca.bin', ubicado en la carpeta 'files', la información de las colecciones pasadas por parámetro
     * @param autorias Colección de autorias
     * @param libros Colección de libros
     * @throws IOException En caso de algún error de E/S
     */
   public static void guardarBin(ArrayList<Autoria> autorias, ArrayList<Libro> libros) throws IOException {
        FileOutputStream fos = new FileOutputStream(new GestionaFicheros().FicheroBin);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(autorias);
        oos.writeObject(libros);
        oos.close();
    }

    /**
     * Guarda en las colecciones de autorías y libros, pasadas por parámetro, la información escrita en el archivo 'biblioteca.bin'
     * @param autorias Colección de autorias
     * @param libros Colección de libros
     * @throws IOException En caso de algún error de E/S
     * @throws ClassNotFoundException En caso de algún error en la llamada a la clase Autoria o Libro
     */
   public static void leerBin(ArrayList<Autoria> autorias, ArrayList<Libro> libros) throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(new GestionaFicheros().FicheroBin);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Autoria> autorias_import = (ArrayList<Autoria>) ois.readObject();
        ArrayList<Libro> libros_import = (ArrayList<Libro>) ois.readObject();
        autorias.clear();
        libros.clear();
        autorias.addAll(autorias_import);
        libros.addAll(libros_import);
        ois.close();
    }
}
