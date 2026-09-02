import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Rover {

    // ========== Atributos de instancia ==========
    private String nombrePropio;
    private double potenciaInicial;
    private double potenciaDisponible;
    private int posicionInicialX;
    private int posicionInicialY;
    private int posicionActualX;
    private int posicionActualY;
    private int cantidadRecargasRealizadas;
    private int contadorDeteccionesFuga;
    private List<List<String>> mandatosExitosos;
    private List<List<String>> mandatosFallidos;
    private double costoMovimiento;
    private double costoDeteccion;
    private int recargasMaximas;
    private String codigoRover;

    // ========== Atributos de clase (estáticos) ==========
    private static int cantidadRoversCreados = 0;
    private static List<Rover> listaRovers = new ArrayList<>();

    // ========== Constructores ==========
    public Rover(String nombrePropio) {
        this(nombrePropio, 100.0);
    }

    public Rover(String nombrePropioP, double potencia) {
        this.nombrePropio = nombrePropioP;
        this.potenciaInicial = potencia;
        this.potenciaDisponible = potencia;
        this.posicionInicialX = 0;
        this.posicionInicialY = 0;
        this.posicionActualX = 0;
        this.posicionActualY = 0;
        this.cantidadRecargasRealizadas = 0;
        this.contadorDeteccionesFuga = 0;
        this.mandatosExitosos = new ArrayList<>();
        this.mandatosFallidos = new ArrayList<>();
        this.costoMovimiento = 0.50;
        this.costoDeteccion = 0.25;
        this.recargasMaximas = 5;
        this.codigoRover = "RVR-" + (System.currentTimeMillis() % 100000);

        // Registro del Rover creado
        cantidadRoversCreados++;
        listaRovers.add(this);
    }

    // ========== Métodos de movimiento ==========
    public void moverIzquierda() {
        if (validarPotenciaActual()) {
            if (!detectarFuga()) {
                posicionActualX -= 1;
                potenciaDisponible -= costoMovimiento;
                registrarMandato("Desplazamiento Izquierda", "Posible");
            } else {
                registrarMandato("Desplazamiento Izquierda", "No posible: fuga detectada");
            }
        } else {
            registrarMandato("Desplazamiento Izquierda", "No posible: potencia insuficiente");
        }
    }

    public void moverDerecha() {
        if (validarPotenciaActual()) {
            if (!detectarFuga()) {
                posicionActualX += 1;
                potenciaDisponible -= costoMovimiento;
                registrarMandato("Desplazamiento Derecha", "Posible");
            } else {
                registrarMandato("Desplazamiento Derecha", "No posible: fuga detectada");
            }
        } else {
            registrarMandato("Desplazamiento Derecha", "No posible: potencia insuficiente");
        }
    }

    public void moverArriba() {
        if (validarPotenciaActual()) {
            if (!detectarFuga()) {
                posicionActualY += 1;
                potenciaDisponible -= costoMovimiento;
                registrarMandato("Desplazamiento Arriba", "Posible");
            } else {
                registrarMandato("Desplazamiento Arriba", "No posible: fuga detectada");
            }
        } else {
            registrarMandato("Desplazamiento Arriba", "No posible: potencia insuficiente");
        }
    }

    public void moverAbajo() {
        if (validarPotenciaActual()) {
            if (!detectarFuga()) {
                posicionActualY -= 1;
                potenciaDisponible -= costoMovimiento;
                registrarMandato("Desplazamiento Abajo", "Posible");
            } else {
                registrarMandato("Desplazamiento Abajo", "No posible: fuga detectada");
            }
        } else {
            registrarMandato("Desplazamiento Abajo", "No posible: potencia insuficiente");
        }
    }

    // ========== Métodos públicos de consulta ==========
    public String consultarPosicionActual() {
        return "Posición actual (x,y): " + posicionActualX + ", " + posicionActualY;
    }

    public double getPotenciaDisponible() {
        return potenciaDisponible;
    }

    public void recargarUnidadesPotencia(double potencia) {
        if (validarRecarga()) {
            potenciaDisponible += potencia;
            cantidadRecargasRealizadas++;
            registrarMandato("Recarga (" + potencia + ")", "Posible");
        } else {
            registrarMandato("Recarga (" + potencia + ")", "No posible: recargas agotadas");
        }
    }

    // ========== Métodos estáticos (para todos los Rovers) ==========
    public static int getCantidadRoversCreados() {
        return cantidadRoversCreados;
    }

    public static String getInformacionTodosLosRovers() {
        if (listaRovers.isEmpty()) {
            return "No se ha creado ningún Rover todavía.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== INFORMACIÓN DE TODOS LOS ROVERS ==========\n");
        sb.append("Total de Rovers creados: ").append(cantidadRoversCreados).append("\n\n");
        for (Rover r : listaRovers) {
            sb.append(r.toString()).append("\n");
        }
        return sb.toString();
    }

    // ========== Métodos privados ==========
    private boolean detectarFuga() {
        contadorDeteccionesFuga++;
        potenciaDisponible -= costoDeteccion;
        Random random = new Random();
        return random.nextDouble() >= 0.5;
    }

    private boolean validarRecarga() {
        return cantidadRecargasRealizadas < recargasMaximas;
    }

    private boolean validarPotenciaActual() {
        double costoMinimo = costoMovimiento + costoDeteccion;
        return potenciaDisponible >= costoMinimo;
    }

    private String determinarFechaHoraActual() {
        Date fecha = new Date(System.currentTimeMillis());
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return formatoFecha.format(fecha);
    }

    private void registrarMandato(String tipoMandato, String estatusMandato) {
        ArrayList<String> mandato = new ArrayList<>();
        mandato.add(tipoMandato);
        mandato.add(estatusMandato);
        mandato.add(determinarFechaHoraActual());

        if ("Posible".equals(estatusMandato)) {
            mandatosExitosos.add(mandato);
        } else {
            mandatosFallidos.add(mandato);
        }
    }

    // ========== toString ==========
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();

        msg.append("========== Ficha del Rover ==========\n");
        msg.append("Código: ").append(codigoRover).append("\n");
        msg.append("Nombre: ").append(nombrePropio).append("\n");
        msg.append("Potencia (inicial/disponible): ")
           .append(String.format("%.2f / %.2f", potenciaInicial, potenciaDisponible)).append("\n");
        msg.append("Posición (inicial → actual): (")
           .append(posicionInicialX).append(",").append(posicionInicialY)
           .append(") → (").append(posicionActualX).append(",").append(posicionActualY).append(")\n");
        msg.append("Costos (mov/detección): ")
           .append(String.format("%.2f / %.2f", costoMovimiento, costoDeteccion)).append("\n");
        msg.append("Recargas (realizadas/máximas): ")
           .append(cantidadRecargasRealizadas).append("/").append(recargasMaximas).append("\n");
        msg.append("Detecciones de fuga realizadas: ").append(contadorDeteccionesFuga).append("\n");
        msg.append("=====================================\n\n");

        // Mandatos exitosos
        msg.append("---- Registro de Mandatos EXITOSOS ----\n");
        msg.append(String.format(" %-4s %-17s %-30s %-20s%n", "N°", "Fecha", "Mandato", "Estado"));
        for (int i = 0; i < mandatosExitosos.size(); i++) {
            List<String> m = mandatosExitosos.get(i);
            String tipo = m.size() > 0 ? m.get(0) : "";
            String estado = m.size() > 1 ? m.get(1) : "";
            String fecha = m.size() > 2 ? m.get(2) : "";
            msg.append(String.format(" %-4d %-17s %-30s %-20s%n", (i + 1), fecha, tipo, estado));
        }
        if (mandatosExitosos.isEmpty()) {
            msg.append(" (sin registros)\n");
        }

        msg.append("\n");

        // Mandatos fallidos
        msg.append("---- Registro de Mandatos FALLIDOS ----\n");
        msg.append(String.format(" %-4s %-17s %-30s %-20s%n", "N°", "Fecha", "Mandato", "Estado"));
        for (int i = 0; i < mandatosFallidos.size(); i++) {
            List<String> m = mandatosFallidos.get(i);
            String tipo = m.size() > 0 ? m.get(0) : "";
            String estado = m.size() > 1 ? m.get(1) : "";
            String fecha = m.size() > 2 ? m.get(2) : "";
            msg.append(String.format(" %-4d %-17s %-30s %-20s%n", (i + 1), fecha, tipo, estado));
        }
        if (mandatosFallidos.isEmpty()) {
            msg.append(" (sin registros)\n");
        }

        return msg.toString();
    }
}