const API_AGENDA = "http://localhost:8081/api";

document.addEventListener("DOMContentLoaded", async () => {
  const form = document.getElementById("tareaForm");
  const btnOptimizar = document.getElementById("btnOptimizar");
  const btnVerificar = document.getElementById("btnVerificar");
  const formViabilidad = document.getElementById("formViabilidad");
  const viabilidadForm = document.getElementById("viabilidadForm");

  const params = new URLSearchParams(window.location.search);
  const idEdicion = params.get("id");

  // === FORMULARIO DE CREAR / EDITAR ===
  if (form) {
    await cargarOpcionesDependencias();

    if (idEdicion) await cargarTareaParaEditar(idEdicion);

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      if (idEdicion) {
        await actualizarTarea(idEdicion);
      } else {
        await guardarTarea(e);
      }
    });
  }

  // === LISTADO DE TAREAS ===
  if (btnOptimizar) {
    await cargarTareas();
    btnOptimizar.addEventListener("click", generarPlan);
  }

  // === FORMULARIO DE VIABILIDAD ===
  if (btnVerificar && formViabilidad && viabilidadForm) {
    // Mostrar u ocultar el formulario de viabilidad
    btnVerificar.addEventListener("click", () => {
      formViabilidad.style.display =
        formViabilidad.style.display === "none" ? "block" : "none";
    });

    // Evento al enviar el formulario de viabilidad
    viabilidadForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const tiempoDisponible = parseFloat(
        document.getElementById("tiempoViabilidad").value
      );
      const clima = document.getElementById("climaViabilidad").value;

      try {
        const url = `${API_AGENDA}/agenda/viabilidad?tiempoDisponible=${tiempoDisponible}&clima=${clima}`;
        const resp = await fetch(url);
        if (!resp.ok) throw new Error("Error al verificar viabilidad");

        const data = await resp.json();
        mostrarResultadoViabilidad(data);
        formViabilidad.style.display = "none"; // Ocultar tras enviar
      } catch (error) {
        console.error("Error verificando viabilidad:", error);
        alert("❌ No se pudo verificar la viabilidad de las tareas.");
      }
    });
  }
});

// ======================
// CREAR TAREA
// ======================
async function guardarTarea(e) {
  const tarea = {
    tareaId: Date.now().toString(),
    nombre: document.getElementById("nombre").value,
    prioridad: document.getElementById("prioridad").value,
    tiempo: parseFloat(document.getElementById("tiempo").value),
    climas: [document.getElementById("clima").value],
    dependencias: document.getElementById("dependencia").value
      ? [document.getElementById("dependencia").value]
      : [],
    fechaLimite: document.getElementById("fechaLimite").value || null,
  };

  try {
    const resp = await fetch(`${API_AGENDA}/tareas`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(tarea),
    });

    if (!resp.ok) throw new Error("Error al guardar tarea");
    alert("✅ Tarea guardada correctamente.");
    e.target.reset();
  } catch (error) {
    console.error("Error guardando tarea:", error);
    alert("❌ No se pudo guardar la tarea.");
  }
}

// ======================
// LISTAR TAREAS
// ======================
async function cargarTareas() {
  try {
    const resp = await fetch(`${API_AGENDA}/tareas`);
    const tareas = await resp.json();

    // Mapa para traducir dependencias (ID -> Nombre)
    const mapaTareas = {};
    tareas.forEach(t => {
      if (t.id) mapaTareas[t.id.toString()] = t.nombre;
      if (t.tareaId) mapaTareas[t.tareaId.toString()] = t.nombre;
      mapaTareas[t.nombre.toLowerCase()] = t.nombre; // también indexamos por nombre
    });

    const tbody = document.querySelector("#tablaTareas tbody");
    tbody.innerHTML = "";

    tareas.forEach(t => {
      let deps = "-";

      if (t.dependencias && t.dependencias.length > 0) {
        // Traducción universal: si es ID o nombre, se resuelve igual
        const nombresDeps = t.dependencias.map(dep => {
          const key = dep.toString().toLowerCase();
          return mapaTareas[key] || mapaTareas[dep] || dep; // prioriza nombre legible
        });

        deps = nombresDeps.join(", ");
      }

      const fila = `
        <tr>
          <td>${t.nombre}</td>
          <td>${t.prioridad}</td>
          <td>${t.tiempo}</td>
          <td>${t.climas ? t.climas.join(", ") : "-"}</td>
          <td>${deps}</td>
          <td>${t.fechaLimite || "-"}</td>
          <td>
            <button class="btn btn-sm btn-outline-primary me-2" onclick="editarTarea('${t.id}')">✏️</button>
            <button class="btn btn-sm btn-outline-danger" onclick="eliminarTarea('${t.id}')">🗑️</button>
          </td>
        </tr>`;
      tbody.innerHTML += fila;
    });
  } catch (error) {
    console.error("Error cargando tareas:", error);
  }
}



// ======================
// EDITAR TAREA
// ======================
function editarTarea(id) {
  window.location.href = `index.html?id=${id}`;
}

async function cargarTareaParaEditar(id) {
  try {
    const resp = await fetch(`${API_AGENDA}/tareas/${id}`);
    if (!resp.ok) throw new Error("No se pudo obtener la tarea.");

    const t = await resp.json();

    document.getElementById("nombre").value = t.nombre;
    document.getElementById("prioridad").value = t.prioridad;
    document.getElementById("tiempo").value = t.tiempo;
    document.getElementById("clima").value = t.climas?.[0] || "cualquiera";
    document.getElementById("dependencia").value = t.dependencias?.[0] || "";
    document.getElementById("fechaLimite").value = t.fechaLimite?.slice(0, 16) || "";

    document.querySelector("button[type='submit']").textContent =
      "Actualizar Tarea";
  } catch (error) {
    console.error("Error cargando tarea para editar:", error);
    alert("❌ No se pudo cargar la tarea.");
  }
}

// ======================
// ACTUALIZAR TAREA
// ======================
async function actualizarTarea(id) {
  const tarea = {
    nombre: document.getElementById("nombre").value,
    prioridad: document.getElementById("prioridad").value,
    tiempo: parseFloat(document.getElementById("tiempo").value),
    climas: [document.getElementById("clima").value],
    dependencias: document.getElementById("dependencia").value
      ? [document.getElementById("dependencia").value]
      : [],
    fechaLimite: document.getElementById("fechaLimite").value || null,
  };

  try {
    const resp = await fetch(`${API_AGENDA}/tareas/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(tarea),
    });

    if (!resp.ok) throw new Error("Error al actualizar tarea");
    alert("✅ Tarea actualizada correctamente.");
    window.location.href = "listado.html";
  } catch (error) {
    console.error("Error actualizando tarea:", error);
    alert("❌ No se pudo actualizar la tarea.");
  }
}

// ======================
// ELIMINAR TAREA
// ======================
async function eliminarTarea(id) {
  if (!confirm("¿Desea eliminar esta tarea?")) return;

  try {
    const resp = await fetch(`${API_AGENDA}/tareas/${id}`, { method: "DELETE" });
    if (!resp.ok) throw new Error("Error al eliminar tarea");
    alert("🗑️ Tarea eliminada correctamente.");
    cargarTareas();
  } catch (error) {
    console.error("Error eliminando tarea:", error);
    alert("❌ No se pudo eliminar la tarea.");
  }
}

// ======================
// PLANIFICAR
// ======================
async function generarPlan() {
  try {
    const resp = await fetch(`${API_AGENDA}/agenda/plan`);
    if (!resp.ok) throw new Error("Error al generar plan");

    const data = await resp.json();
    mostrarResultadoPlan(data);
  } catch (error) {
    console.error("Error generando plan:", error);
    alert("❌ No se pudo generar el plan optimizado.");
  }
}

function mostrarResultadoPlan(data) {
  const contenedor = document.getElementById("resultadoPlan");
  contenedor.innerHTML = "";

  if (!data) {
    contenedor.innerHTML = `<div class="alert alert-danger">❌ No se recibió respuesta del servidor.</div>`;
    return;
  }

  let html = `
    <div class="card shadow-sm p-4">
      <h4 class="text-primary mb-3">📋 Resultado del Plan</h4>
      <p><strong>Estado:</strong> ${data.estado}</p>
      <p><strong>Mensaje:</strong> ${data.mensaje}</p>
      <p><strong>Tiempo total:</strong> ${data.tiempoTotal ?? 0} horas</p>
      <p><strong>Cantidad de tareas:</strong> ${data.cantidadTareas ?? 0}</p>`;

  if (data.tareasPlanificadas?.length) {
    html += `
      <div class="table-responsive">
        <table class="table table-bordered table-hover align-middle mt-3">
          <thead class="table-success">
            <tr>
              <th>Nombre</th>
              <th>Prioridad</th>
              <th>Tiempo (h)</th>
              <th>Hora inicio</th>
              <th>Hora fin</th>
            </tr>
          </thead>
          <tbody>
            ${data.tareasPlanificadas
      .map(
        (t) => `
              <tr>
                <td>${t.nombre}</td>
                <td>${t.prioridad}</td>
                <td>${t.tiempoEstimado}</td>
                <td>${t.horaInicio}</td>
                <td>${t.horaFin}</td>
              </tr>`
      )
      .join("")}
          </tbody>
        </table>
      </div>`;
  } else {
    html += `<div class="alert alert-warning mt-3">⚠️ No se generaron tareas planificadas.</div>`;
  }

  html += `</div>`;
  contenedor.innerHTML = html;
}

// ======================
// RESULTADO DE VIABILIDAD
// ======================
function mostrarResultadoViabilidad(data) {
  const contenedor = document.getElementById("resultadoPlan");
  contenedor.innerHTML = "";

  if (!data) {
    contenedor.innerHTML = `<div class="alert alert-danger">❌ No se recibió respuesta del servidor.</div>`;
    return;
  }

  contenedor.innerHTML = `
    <div class="card shadow-sm p-4">
      <h4 class="text-info mb-3">📊 Verificación de Viabilidad</h4>
      <p><strong>Resultado:</strong> ${data.resultado}</p>
      <p><strong>Mensaje:</strong> ${data.mensaje}</p>
      <p><strong>Tiempo total necesario:</strong> ${data.tiempoTotal ?? 0} h</p>
      <p><strong>Tiempo disponible:</strong> ${data.tiempoDisponible ?? 0} h</p>
      <p><strong>Tareas posibles:</strong> ${data.tareasPosibles ?? 0} de ${
    data.tareasTotales ?? 0
  }</p>
    </div>`;
}

// ======================
// CARGAR DEPENDENCIAS
// ======================
async function cargarOpcionesDependencias() {
  try {
    const resp = await fetch(`${API_AGENDA}/tareas`);
    if (!resp.ok) throw new Error("Error al obtener tareas existentes");

    const tareas = await resp.json();
    const select = document.getElementById("dependencia");

    select.innerHTML = `<option value="">Ninguna</option>`;

    tareas.forEach((t) => {
      const option = document.createElement("option");
      option.value = t.tareaId || t.id;
      option.textContent = t.nombre;
      select.appendChild(option);
    });
  } catch (error) {
    console.error("Error cargando dependencias:", error);
  }
}
