export class Todo {
    public id: string;
    public description: string;
    public completed: boolean;

    constructor(id: string, description: string, completed: boolean) {
        this.id = id;
        this.description = description;
        this.completed = completed;
    }
}