# Cosas Pendientes

## product-service

- Para los Details, Brand, Categories y Tags cuando vaya a insertar busque nombres similares
- En vez de retorna Null en los servicios, retornar un Optional.empty()
- Delete IEntityService para el manejo de entidades ya que cada entidad funciona diferentes (dará problemas mas adelante con cada alguna cosa mas compleja a futuro)

## general

- Implementar un LoggerUtils para hacer Logs con mensajes mas adelante (para hacer logs de errores, info, debug, etc) (-)
- User @RepositoryRestResource para Testing de otro servicios
- Agregar una librearía de Validaciones y hacer en las entidades donde sea necesario tener validaciones complejas entidades (2)
- Utilizar ResponseEntity en todos los Controllers (1)

## Comentarios

Entidades/Microservicios básicas para empezar ya con el Front

- Product
- Order
- Customer - Buyer & StoreOwner
- UserCredentials
- Cart Service
