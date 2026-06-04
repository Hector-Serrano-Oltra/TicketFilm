var DB = {
  peliculas: [],
  anuncios: [],
  cines: [],
  categorias: [],
  usuarioLogueado: null,

  cargar: async function() {
    try {
      var r1 = await fetch('/api/peliculas');
      DB.peliculas = await r1.json();
      var r2 = await fetch('/api/anuncios');
      DB.anuncios = await r2.json();
      var r3 = await fetch('/api/cines');
      DB.cines = await r3.json();
      var r4 = await fetch('/api/categorias');
      DB.categorias = await r4.json();
    } catch (e) {
      console.error('Error cargando datos:', e);
    }
  },

  buscarCine: function(pk) {
    for (var i = 0; i < DB.cines.length; i++) { if (DB.cines[i].pk == pk) return DB.cines[i]; }
    return null;
  },

  buscarPelicula: function(pk) {
    for (var i = 0; i < DB.peliculas.length; i++) { if (DB.peliculas[i].pk == pk) return DB.peliculas[i]; }
    return null;
  },

  login: async function(dni, pass) {
    try {
      var r = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ dni: dni, pass: pass })
      });
      if (r.ok) return await r.json();
    } catch (e) { console.error(e); }
    return null;
  },

  registrar: async function(dni, nombre, email, pass, telefono) {
    try {
      var r = await fetch('/api/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ dni: dni, nombre: nombre, email: email, pass: pass, telefono: telefono })
      });
      if (r.ok) return await r.json();
      if (r.status === 409) return { error: 'duplicado' };
    } catch (e) { console.error(e); }
    return null;
  },

  comprar: async function(peliculaId, cantidad) {
    try {
      var r = await fetch('/api/comprar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ pelicula_id: peliculaId, cantidad: cantidad })
      });
      var data = await r.json();
      if (r.ok) return data;
      return data; // devuelve el error con .error y .disponibles
    } catch (e) { console.error(e); return { error: 'Error de conexión' }; }
  }
};
