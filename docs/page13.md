MethodHandles.Lookup the methods on the class.
Acquire a MethodHandle to invoke:
Method type approach ::
Describe the method to create a MethodType
find*()method to extract a MethodHandle
Method signature approach ::
Get a Method instance by name and arity
unreflect() the Method instance to extract a MethodHandle.