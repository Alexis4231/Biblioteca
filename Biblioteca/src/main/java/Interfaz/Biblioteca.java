package Interfaz;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import Biblioteca.Autoria;
import Biblioteca.Libro;
import Ficheros.GestionaFicheros;

public class Biblioteca {
    private static ArrayList<Autoria> autorias = new ArrayList<>();
    private static ArrayList<Libro> libros = new ArrayList<>();
    private Scanner sc;

    public Biblioteca() {
        sc = new Scanner(System.in);
    }

    public ArrayList<Autoria> getAutorias() {
        return autorias;
    }

    /**
     * Añade un autor a la colección de autores
     * @param autor Objeto autor a añadir
     */
    private void anadirAutoria(Autoria autor) {
        autorias.add(autor);
    }

    /**
     * Devuelve la colección de libros
     * @return libros Colección de libros
     */
    private ArrayList<Libro> getLibros() {
        return libros;
    }

    /**
     * Crea un nuevo autor, y lo añade a la colección de autores
     */
    private void crearAutor() {
        boolean repetidor = false;
        int id = -1;
        do {
            if (!repetidor) {
                System.out.print("Introduce el ID del autor: ");
            } else {
                repetidor = false;
                System.out.println("El ID que has introducido ya existe!!");
                System.out.print("Vuelve a introducir el ID del autor: ");
            }
            id = numeroScanner();
            for (Autoria a : getAutorias()) {
                if (id == a.getId()) {
                    repetidor = true;
                }
            }
        } while (repetidor);
        System.out.print("Introduce el nombre del autor ");
        String nombre = sc.next();
        System.out.print("Introduce el apellido del autor ");
        String apellido = sc.next();
        anadirAutoria(new Autoria(id, nombre, apellido));
    }

    /**
     * Crea un nuevo libro y lo añade a la colección de libros
     */
    private void crearLibro() {
        boolean repetidor = false;
        String isbn = "";
        do {
            if (!repetidor) {
                System.out.print("Introduce el ISBN del libro: ");
            } else {
                repetidor = false;
                System.out.println("El ISBN que has introducido ya existe!!");
                System.out.print("Vuelve a introducir el ISBN del libro: ");
            }
            isbn = sc.next();
            for (Libro l : getLibros()) {
                if (isbn.equals(l.getIsbn())) {
                    repetidor = true;
                }
            }
        } while (repetidor);
        System.out.print("Introduce el titulo del libro: ");
        String titulo = sc.next();
        System.out.print("Introduce el ID del autor: ");
        int id = numeroScanner();
        Autoria autor = null;
        for (Autoria a : getAutorias()) {
            if (a.getId() == id) {
                autor = a;
            }
        }
        if (autor != null) {
            getLibros().add(new Libro(isbn, titulo, autor));
        } else {
            System.out.println("El ID introducido no existe");
            esperar();
        }
    }

    /**
     * Muestra todos los autores existentes en la colección de autores
     */
    private void verAutores() {
        boolean noAutores = true;
        for (Autoria a : getAutorias()) {
            System.out.println(a.toString());
            noAutores = false;
        }
        if (noAutores) {
            System.out.println("No existen autores!!");
        }
        esperar();
    }

    /**
     * Muestra todos los libros existentes en la colección de libros
     */
    private void verLibros() {
        boolean noLibros = true;
        for (Libro l : getLibros()) {
            System.out.println(l.toString());
            noLibros = false;
        }
        if (noLibros) {
            System.out.println("No existen libros!!");
        }
        esperar();
    }

    /**
     * Elimina un libro de la colección identificándolo por su ISBN
     */
    private void eliminarLibro() {
        System.out.print("Introduce el ISBN del libro a eliminar: ");
        boolean encontrado = false;
        Libro libro_eliminar = null;
        String isbn_eliminar = sc.next();
        for (Libro l : getLibros()) {
            if (isbn_eliminar.equals(l.getIsbn())) {
                encontrado = true;
                libro_eliminar = l;
            }
        }
        if (encontrado) {
            getLibros().remove(libro_eliminar);
        } else {
            System.out.println("El ISBN introducido no existe!!");
            esperar();
        }
    }

    /**
     * Exporta la información de las colecciones a un fichero de texto introducido por el usuario
     */
    private void exportarFicheroTexto() {
        System.out.print("Introduce el archivo: ");
        String f = sc.next();
        while(f.equals("files/biblioteca.bin") || f.equals("./files/biblioteca.bin")){
            System.out.println("No puedes realizar la exportacion sobre este fichero!!");
            System.out.print("Introduce el archivo: ");
            f = sc.next();
        }
        File file = new File(f);
        boolean noexiste = !file.exists();
        boolean sobreescribir = false;
        boolean salir = false;
        if (file.exists()) {
            System.out.println("El archivo ya existe");
            while (!salir) {
                System.out.print("¿Quieres sobreescribirlo? (Y/N): ");
                String sobreescribir_eleccion = sc.next();
                if (sobreescribir_eleccion.toLowerCase().equals("y")) {
                    sobreescribir = true;
                    salir = true;
                } else if (sobreescribir_eleccion.toLowerCase().equals("n")) {
                    salir = true;
                } else {
                    System.out.print("Por favor, introduce una opcion valida");
                }
            }
        }
        if (sobreescribir || noexiste) {
            try {
                GestionaFicheros.exportar(file, getAutorias(), getLibros());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Importa la información del archivo de texto especificado por el usuario, creando las colecciones autorías y libros
     */
    private void importarFicheroTexto() {
        System.out.print("Introduce el archivo: ");
        String f = sc.next();
        File file = new File(f);
        try {
            GestionaFicheros.importar(file, getAutorias(), getLibros());
        } catch (FileNotFoundException e){
            System.out.println("Archivo no encontrado!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exporta al fichero 'biblioteca.bin', ubicado en la carpeta 'files', las colecciones en formato binario
     */
    private void guardarFicheroBinario() {
        try {
            GestionaFicheros.guardarBin(getAutorias(), getLibros());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Importa del fichero 'bibliotecas.bin' la información, en formato binario, de las colecciones, creándolas
     */
    private void leerFicheroBinario() {
        try {
            GestionaFicheros.leerBin(getAutorias(), getLibros());
        } catch (IOException e) {
            e.getStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recoge un número entero introducido por el usuario mediante un Scanner.
     * @return numero Número entero que ha recogido el Scanner
     */
    private static int numeroScanner() {
        int numero = 0;
        boolean continuar = false;
        Scanner sc = new Scanner(System.in);
        while (!continuar) {
            try {
                numero = sc.nextInt();
                continuar = true;
            } catch (InputMismatchException e) {
                System.out.print("Por favor introduce un numero entero: ");
                sc.next();
            }
        }
        return numero;
    }

    /**
     * Provoca que el programa se detenga por cinco segundos con la intención de ayudar a una lectura más comoda por parte del usuario de la información lanzada por el programa
     */
    private static void esperar() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Biblioteca b = new Biblioteca();
        File f = new File("./files/biblioteca.bin");
        if (f.exists()) {
            b.leerFicheroBinario();
        }
        boolean seguir = true;
        int eleccion = -1;
        while (seguir) {
            System.out.println("MENU");
            System.out.println("1- Crear autor");
            System.out.println("2- Ver autor");
            System.out.println("3- Crear libro");
            System.out.println("4- Mostrar libro");
            System.out.println("5- Eliminar libro");
            System.out.println("6- Exportar a fichero de texto");
            System.out.println("7- Importar de fichero de texto");
            System.out.println("8- Guardar en fichero binario");
            System.out.println("9- Leer de fichero binario");
            System.out.println("0- Salir");
            System.out.print("Introduce una opcion: ");
            eleccion = numeroScanner();
            switch (eleccion) {
                case 1:
                    b.crearAutor();
                    break;
                case 2:
                    b.verAutores();
                    break;
                case 3:
                    b.crearLibro();
                    break;
                case 4:
                    b.verLibros();
                    break;
                case 5:
                    b.eliminarLibro();
                    break;
                case 6:
                    b.exportarFicheroTexto();
                    break;
                case 7:
                    b.importarFicheroTexto();
                    break;
                case 8:
                    b.guardarFicheroBinario();
                    break;
                case 9:
                    b.leerFicheroBinario();
                    break;
                case 0:
                    b.guardarFicheroBinario();
                    seguir = false;
                    break;
            }
        }
    }
}