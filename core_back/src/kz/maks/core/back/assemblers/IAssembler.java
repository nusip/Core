package kz.maks.core.back.assemblers;

public interface IAssembler<FROM, TO> {

    TO assemble(FROM from, TO to);

}
