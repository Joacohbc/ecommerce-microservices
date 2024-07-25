# Cosas Pendientes

## product-service
- Para los Details, Brand, Categories y Tags cuando vaya a insertar busque nombres similares 
- En vez de retorna Null en los servicios, retornar un Optional.empty()
- Delete IEntityService para el manejo de entidades ya que cada entidad funciona diferentes (dara problemas mas adelante con cada alguna cosa mas compleja a futuro)


## general
- Implementar un LoggerUtils para hacer Logs con mensajes mas adelante (para hacer logs de errores, info, debug, etc)
- User @RepositoryRestResource para Testing de otro servicios
- Implementar CustomJPARepository para como Repositorio de los servicios
- Mover DTOs a un paquete de entidades