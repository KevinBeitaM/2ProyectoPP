
% PREDICADOS AUXILIARES BASE

% subset/2 - Verifica que todos los elementos de una lista estén en otra
subset([], _).
subset([H|T], Conjunto) :-
    member(H, Conjunto),
    subset(T, Conjunto).


% PREDICADO EJEMPLO (testing)

suma(X, Y, Z) :- Z is X + Y.


% DEFINICIONES Y CONFIGURACIONES BASE


% Tipos de prioridad válidos
prioridad_valida(alta).
prioridad_valida(media).
prioridad_valida(baja).

% Tipos de clima válidos
clima_valido(soleado).
clima_valido(nublado).
clima_valido(lluvioso).
clima_valido(cualquiera).

% Valor numérico de prioridad
valor_prioridad(alta, 3).
valor_prioridad(media, 2).
valor_prioridad(baja, 1).


% VALIDACIONES DE TAREAS


validar_tarea(tarea(Id, Nombre, Prioridad, Tiempo, Climas, Dependencias)) :-
    atom(Id),
    atom(Nombre),
    prioridad_valida(Prioridad),
    number(Tiempo),
    Tiempo > 0,
    is_list(Climas),
    validar_climas(Climas),
    is_list(Dependencias).

validar_climas([]).
validar_climas([C|Cs]) :-
    clima_valido(C),
    validar_climas(Cs).

validar_lista_tareas([]).
validar_lista_tareas([T|Ts]) :-
    validar_tarea(T),
    validar_lista_tareas(Ts).


% VERIFICACIÓN DE DEPENDENCIAS


tiene_dependencias_circulares(Tareas) :-
    member(tarea(Id, _, _, _, _, Deps), Tareas),
    member(DepId, Deps),
    dependencia_circular(Id, DepId, Tareas, [Id]).

dependencia_circular(Id, Id, _, _) :- !.
dependencia_circular(Origen, Actual, Tareas, Visitados) :-
    member(tarea(Actual, _, _, _, _, Deps), Tareas),
    member(Siguiente, Deps),
    \+ member(Siguiente, Visitados),
    dependencia_circular(Origen, Siguiente, Tareas, [Actual|Visitados]).


% VERIFICACIÓN DE CLIMA


tarea_compatible_clima(tarea(_, _, _, _, Climas, _), ClimaActual) :-
    (member(cualquiera, Climas) ; member(ClimaActual, Climas)).


% DEPENDENCIAS


ordenar_por_dependencias(Tareas, TareasOrdenadas) :-
    ordenamiento_topologico(Tareas, [], TareasOrdenadas).

ordenamiento_topologico([], Ordenadas, Ordenadas).
ordenamiento_topologico(Pendientes, Acumuladas, Resultado) :-
    ids_acumuladas(Acumuladas, IdsCompletadas),
    seleccionar_sin_dependencias(Pendientes, IdsCompletadas, Seleccionadas, Restantes),
    Seleccionadas \= [],
    append(Acumuladas, Seleccionadas, NuevasAcumuladas),
    ordenamiento_topologico(Restantes, NuevasAcumuladas, Resultado).

ids_acumuladas([], []).
ids_acumuladas([tarea(Id, _, _, _, _, _)|Ts], [Id|Ids]) :-
    ids_acumuladas(Ts, Ids).


% Caso base: lista vacía
seleccionar_sin_dependencias([], _, [], []).

% Caso: la tarea SÍ cumple dependencias (la incluimos en Seleccionadas)
seleccionar_sin_dependencias([T|Ts], Completadas, [T|Sel], Rest) :-
    T = tarea(_, _, _, _, _, Deps),
    subset(Deps, Completadas),
    !,
    seleccionar_sin_dependencias(Ts, Completadas, Sel, Rest).

% Caso: la tarea NO cumple dependencias (la incluimos en Restantes)
seleccionar_sin_dependencias([T|Ts], Completadas, Sel, [T|Rest]) :-
    T = tarea(_, _, _, _, _, Deps),
    \+ subset(Deps, Completadas),
    seleccionar_sin_dependencias(Ts, Completadas, Sel, Rest).


%  ORDENAMIENTO POR PRIORIDAD


comparar_prioridad(>, tarea(_, _, P1, _, _, _), tarea(_, _, P2, _, _, _)) :-
    valor_prioridad(P1, V1),
    valor_prioridad(P2, V2),
    V1 < V2.
comparar_prioridad(<, tarea(_, _, P1, _, _, _), tarea(_, _, P2, _, _, _)) :-
    valor_prioridad(P1, V1),
    valor_prioridad(P2, V2),
    V1 > V2.
comparar_prioridad(=, tarea(_, _, P, _, _, _), tarea(_, _, P, _, _, _)).

ordenar_por_prioridad(Tareas, TareasOrdenadas) :-
    predsort(comparar_prioridad, Tareas, TareasOrdenadas).


% CÁLCULO DE TIEMPO TOTAL


calcular_tiempo_total([], 0).
calcular_tiempo_total([tarea(_, _, _, Tiempo, _, _)|Ts], Total) :-
    calcular_tiempo_total(Ts, SubTotal),
    Total is SubTotal + Tiempo.


% GENERACIÓN DE PLAN OPTIMIZADO


generar_plan(Tareas, TiempoDisponible, ClimaActual, HoraInicio, Plan, Estado) :-
    validar_lista_tareas(Tareas),
    \+ tiene_dependencias_circulares(Tareas),
    filtrar_por_clima(Tareas, ClimaActual, TareasFiltradas),
    ordenar_por_dependencias(TareasFiltradas, TareasConDep),
    ordenar_con_prioridad_y_dependencias(TareasConDep, TareasOrdenadas),
    asignar_horarios(TareasOrdenadas, TiempoDisponible, HoraInicio, Plan, Estado).

filtrar_por_clima([], _, []).
filtrar_por_clima([T|Ts], Clima, [T|Filtradas]) :-
    tarea_compatible_clima(T, Clima),
    filtrar_por_clima(Ts, Clima, Filtradas).
filtrar_por_clima([T|Ts], Clima, Filtradas) :-
    \+ tarea_compatible_clima(T, Clima),
    filtrar_por_clima(Ts, Clima, Filtradas).

ordenar_con_prioridad_y_dependencias(Tareas, TareasOrdenadas) :-
    ordenar_por_grupos_dependencias(Tareas, [], TareasOrdenadas).

ordenar_por_grupos_dependencias([], Acum, Acum).
ordenar_por_grupos_dependencias(Restantes, Acum, Resultado) :-
    ids_acumuladas(Acum, Completadas),
    seleccionar_sin_dependencias(Restantes, Completadas, Disponibles, Pendientes),
    Disponibles \= [],
    ordenar_por_prioridad(Disponibles, DisponiblesOrdenadas),
    append(Acum, DisponiblesOrdenadas, NuevoAcum),
    ordenar_por_grupos_dependencias(Pendientes, NuevoAcum, Resultado).

asignar_horarios(Tareas, TiempoDisponible, HoraInicio, Plan, Estado) :-
    asignar_horarios_aux(Tareas, TiempoDisponible, HoraInicio, [], Plan, Estado).

asignar_horarios_aux([], _, _, Plan, Plan, completo).
asignar_horarios_aux([T|Ts], TiempoRestante, HoraActual, Acum, Plan, Estado) :-
    T = tarea(Id, Nombre, Prioridad, Duracion, Climas, Deps),
    Duracion =< TiempoRestante,
    !,
    HoraFin is HoraActual + Duracion,
    TareaConHorario = tarea_planificada(Id, Nombre, Prioridad, Duracion, Climas, Deps, HoraActual, HoraFin),
    append(Acum, [TareaConHorario], NuevoAcum),
    NuevoTiempoRestante is TiempoRestante - Duracion,
    asignar_horarios_aux(Ts, NuevoTiempoRestante, HoraFin, NuevoAcum, Plan, Estado).
asignar_horarios_aux([_|Ts], TiempoRestante, HoraActual, Acum, Plan, parcial) :-
    asignar_horarios_aux(Ts, TiempoRestante, HoraActual, Acum, Plan, _).
asignar_horarios_aux([_|_], 0, _, Plan, Plan, parcial).


% VERIFICACIÓN DE VIABILIDAD


verificar_viabilidad(Tareas, TiempoDisponible, ClimaActual, Resultado) :-
    filtrar_por_clima(Tareas, ClimaActual, TareasFiltradas),
    ordenar_por_dependencias(TareasFiltradas, TareasOrdenadas),
    calcular_tiempo_total(TareasOrdenadas, TiempoTotal),
    determinar_viabilidad(TiempoTotal, TiempoDisponible, TareasFiltradas, Resultado).

determinar_viabilidad(TiempoTotal, TiempoDisponible, _, viable) :-
    TiempoTotal =< TiempoDisponible, !.
determinar_viabilidad(_, _, [], imposible) :- !.
determinar_viabilidad(_, _, _, parcial).


% REPLANIFICACIÓN


replanificar(TareasOriginales, TareasCompletadas, TiempoDisponible, ClimaActual, HoraInicio, NuevoPlan, Estado) :-
    obtener_ids_completadas(TareasCompletadas, IdsCompletadas),
    filtrar_tareas_pendientes(TareasOriginales, IdsCompletadas, TareasPendientes),
    generar_plan(TareasPendientes, TiempoDisponible, ClimaActual, HoraInicio, NuevoPlan, Estado).

obtener_ids_completadas([], []).
obtener_ids_completadas([Id|Ids], [Id|IdsResult]) :-
    obtener_ids_completadas(Ids, IdsResult).

filtrar_tareas_pendientes([], _, []).
filtrar_tareas_pendientes([tarea(Id, _, _, _, _, _)|Ts], Completadas, Pendientes) :-
    member(Id, Completadas),
    filtrar_tareas_pendientes(Ts, Completadas, Pendientes).
filtrar_tareas_pendientes([T|Ts], Completadas, [T|Pendientes]) :-
    T = tarea(Id, _, _, _, _, _),
    \+ member(Id, Completadas),
    filtrar_tareas_pendientes(Ts, Completadas, Pendientes).